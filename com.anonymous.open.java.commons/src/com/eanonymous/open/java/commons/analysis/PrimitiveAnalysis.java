package com.eanonymous.open.java.commons.analysis;

import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.script.Common;
import com.ensoftcorp.atlas.core.xcsg.XCSG;

/**
 * Some utilities for dealing with autoboxing and unboxing
 * Reference: https://docs.oracle.com/javase/tutorial/java/data/autoboxing.html
 * 
 * @author Ben Holland
 */
public class PrimitiveAnalysis {
	
	private PrimitiveAnalysis(){
		// use to prevent object construction
	}

	// boxed primitive types
	private static final Node BOOLEAN_OBJECT = Common.typeSelect("java.lang","Boolean").eval().nodes().one();
	private static final Node CHARACTER_OBJECT = Common.typeSelect("java.lang", "Character").eval().nodes().one();
	private static final Node BYTE_OBJECT = Common.typeSelect("java.lang", "Byte").eval().nodes().one();
	private static final Node DOUBLE_OBJECT = Common.typeSelect("java.lang", "Double").eval().nodes().one();
	private static final Node FLOAT_OBJECT = Common.typeSelect("java.lang", "Float").eval().nodes().one();
	private static final Node INTEGER_OBJECT = Common.typeSelect("java.lang", "Integer").eval().nodes().one();
	private static final Node LONG_OBJECT = Common.typeSelect("java.lang", "Long").eval().nodes().one();
	private static final Node SHORT_OBJECT = Common.typeSelect("java.lang", "Short").eval().nodes().one();
	
	// primitive types
	private static final Node BOOLEAN_PRIMITIVE =  Common.universe().nodesTaggedWithAny(XCSG.Primitive).selectNode(XCSG.name, "boolean").eval().nodes().one();
	private static final Node CHARACTER_PRIMITIVE = Common.universe().nodesTaggedWithAny(XCSG.Primitive).selectNode(XCSG.name, "char").eval().nodes().one();
	private static final Node BYTE_PRIMITIVE = Common.universe().nodesTaggedWithAny(XCSG.Primitive).selectNode(XCSG.name, "byte").eval().nodes().one();
	private static final Node DOUBLE_PRIMITIVE = Common.universe().nodesTaggedWithAny(XCSG.Primitive).selectNode(XCSG.name, "double").eval().nodes().one();
	private static final Node FLOAT_PRIMITIVE = Common.universe().nodesTaggedWithAny(XCSG.Primitive).selectNode(XCSG.name, "float").eval().nodes().one();
	private static final Node INTEGER_PRIMITIVE = Common.universe().nodesTaggedWithAny(XCSG.Primitive).selectNode(XCSG.name, "int").eval().nodes().one();
	private static final Node LONG_PRIMITIVE = Common.universe().nodesTaggedWithAny(XCSG.Primitive).selectNode(XCSG.name, "long").eval().nodes().one();
	private static final Node SHORT_PRIMITIVE = Common.universe().nodesTaggedWithAny(XCSG.Primitive).selectNode(XCSG.name, "short").eval().nodes().one();
	
	/**
	 * Returns true if the given type is a primitive or primitive object type
	 * @param primitiveType
	 * @return
	 */
	public static boolean isBoxablePrimitiveType(Node type){
		if (type.equals(BOOLEAN_PRIMITIVE) || type.equals(BOOLEAN_OBJECT)) {
			return true;
		} else if (type.equals(CHARACTER_PRIMITIVE) || type.equals(CHARACTER_OBJECT)) {
			return true;
		} else if (type.equals(BYTE_PRIMITIVE) || type.equals(BYTE_OBJECT)) {
			return true;
		} else if (type.equals(DOUBLE_PRIMITIVE) || type.equals(DOUBLE_OBJECT)) {
			return true;
		} else if (type.equals(FLOAT_PRIMITIVE) || type.equals(FLOAT_OBJECT)) {
			return true;
		} else if (type.equals(INTEGER_PRIMITIVE) || type.equals(INTEGER_OBJECT)) {
			return true;
		} else if (type.equals(LONG_PRIMITIVE) || type.equals(LONG_OBJECT)) {
			return true;
		} else if (type.equals(SHORT_PRIMITIVE) || type.equals(SHORT_OBJECT)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Converts a primitive type to the boxed object equivalent
	 * @param primitiveType
	 * @return
	 */
	public static Node autobox(Node primitiveType) {
		if (primitiveType == BOOLEAN_PRIMITIVE) {
			return BOOLEAN_OBJECT;
		} else if (primitiveType == CHARACTER_PRIMITIVE) {
			return CHARACTER_OBJECT;
		} else if (primitiveType == BYTE_PRIMITIVE) {
			return BYTE_OBJECT;
		} else if (primitiveType == DOUBLE_PRIMITIVE) {
			return DOUBLE_OBJECT;
		} else if (primitiveType == FLOAT_PRIMITIVE) {
			return FLOAT_OBJECT;
		} else if (primitiveType == INTEGER_PRIMITIVE) {
			return INTEGER_OBJECT;
		} else if (primitiveType == LONG_PRIMITIVE) {
			return LONG_OBJECT;
		} else if (primitiveType == SHORT_PRIMITIVE) {
			return SHORT_OBJECT;
		} else {
			throw new RuntimeException("Unexpected primitive type!\n" + primitiveType);
		}
	}
	
	/**
	 * Converts a primitive wrapper object type to the unboxed primitive equivalent
	 * @param primitiveType
	 * @return
	 */
	public static Node unbox(Node wrapperType) {
		if (wrapperType == BOOLEAN_OBJECT) {
			return BOOLEAN_PRIMITIVE;
		} else if (wrapperType == CHARACTER_OBJECT) {
			return CHARACTER_PRIMITIVE;
		} else if (wrapperType == BYTE_OBJECT) {
			return BYTE_PRIMITIVE;
		} else if (wrapperType == DOUBLE_OBJECT) {
			return DOUBLE_PRIMITIVE;
		} else if (wrapperType == FLOAT_OBJECT) {
			return FLOAT_PRIMITIVE;
		} else if (wrapperType == INTEGER_OBJECT) {
			return INTEGER_PRIMITIVE;
		} else if (wrapperType == LONG_OBJECT) {
			return LONG_PRIMITIVE;
		} else if (wrapperType == SHORT_OBJECT) {
			return SHORT_PRIMITIVE;
		} else {
			throw new RuntimeException("Unexpected primitive wrapper object type!\n" + wrapperType);
		}
	}
	
}
