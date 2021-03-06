package org.zenframework.z8.pde.debug.model.value;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IIndexedValue;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;

import org.zenframework.z8.pde.debug.JDXMessages;
import org.zenframework.z8.pde.debug.model.JDXDebugTarget;
import org.zenframework.z8.pde.debug.model.JDXThread;
import org.zenframework.z8.pde.debug.model.variable.JDXArrayEntryVariable;
import org.zenframework.z8.pde.debug.model.variable.JDXVariable;

import com.sun.jdi.IntegerValue;
import com.sun.jdi.Method;
import com.sun.jdi.ObjectCollectedException;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.Value;

public class JDXArrayValue extends JDXObjectValue implements IIndexedValue {
	private int m_length = -1;

	public JDXArrayValue(JDXDebugTarget target, JDXThread thread, JDXVariable variable, ObjectReference value) {
		super(target, thread, variable, value);
	}

	public IValue[] getValues() throws DebugException {
		List<Value> list = getUnderlyingValues();

		int count = list.size();

		IValue[] values = new IValue[count];

		JDXDebugTarget target = getJDXDebugTarget();

		for(int i = 0; i < count; i++) {
			Value value = (Value)list.get(i);
			values[i] = JDXValue.createValue(target, getJDXThread(), getJDXVariable(), value);
		}
		return values;
	}

	public IValue getValue(int index) throws DebugException {
		Value v = getUnderlyingValue(index);
		return JDXValue.createValue(getJDXDebugTarget(), getJDXThread(), getJDXVariable(), v);
	}

	public synchronized int getLength() throws DebugException {
		ObjectReference object = getUnderlyingObject();
		ReferenceType type = object.referenceType();

		List<Method> method = type.methodsByName("size", "()I");

		assert (method.size() == 1);

		Value intValue = getJDXThread().invokeMethod(object, method.get(0), new ArrayList<Value>());

		m_length = ((IntegerValue)intValue).value();

		return m_length;
	}

	public void setValue(int index, IValue value) {
		assert (false);
	}

	public Value getUnderlyingValue(int index) throws DebugException {
		ObjectReference object = (ObjectReference)getUnderlyingValue();
		ReferenceType type = object.referenceType();

		List<Method> method = type.methodsByName("get", "(I)Ljava/lang/Object;");

		assert (method.size() == 1);

		Value arg = object.virtualMachine().mirrorOf(index);
		List<Value> args = new ArrayList<Value>();
		args.add(arg);

		return getJDXThread().invokeMethod(object, method.get(0), args);
	}

	protected List<Value> getUnderlyingValues() throws DebugException {
		List<Value> values = new ArrayList<Value>();

		for(int i = 0; i < getLength(); i++) {
			values.add(getUnderlyingValue(i));
		}

		return values;
	}

	@Override
	public int getSize() throws DebugException {
		return getLength();
	}

	@Override
	public IVariable getVariable(int offset) throws DebugException {
		if(offset >= getLength()) {
			requestFailed(JDXMessages.JDXArrayValue_6, null);
		}
		return new JDXArrayEntryVariable(getJDXDebugTarget(), getJDXThread(), this, offset);
	}

	@Override
	public IVariable[] getVariables(int offset, int length) throws DebugException {
		if(offset >= getLength()) {
			requestFailed(JDXMessages.JDXArrayValue_6, null);
		}
		if((offset + length - 1) >= getLength()) {
			requestFailed(JDXMessages.JDXArrayValue_8, null);
		}
		IVariable[] variables = new IVariable[length];
		int index = offset;
		for(int i = 0; i < length; i++) {
			variables[i] = new JDXArrayEntryVariable(getJDXDebugTarget(), getJDXThread(), this, index);
			index++;
		}
		return variables;
	}

	@Override
	protected synchronized List<IVariable> getVariablesList() throws DebugException {
		List<IVariable> variables = new ArrayList<IVariable>();

		try {
			int length = getLength();

			ArrayList<IVariable> list = new ArrayList<IVariable>(length);

			for(int i = 0; i < length; i++) {
				list.add(new JDXArrayEntryVariable(getJDXDebugTarget(), getJDXThread(), this, i));
			}

			variables = list;
		} catch(DebugException e) {
			if(e.getCause() instanceof ObjectCollectedException) {
				return new ArrayList<IVariable>();
			}
			throw e;
		}

		return variables;
	}

	@Override
	public int getInitialOffset() {
		return 0;
	}

	@Override
	public String getReferenceTypeName() throws DebugException {
		return getJDXVariable().getReferenceTypeName();
	}
}
