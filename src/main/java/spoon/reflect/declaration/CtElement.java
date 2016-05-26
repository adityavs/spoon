/**
 * Copyright (C) 2006-2015 INRIA and contributors
 * Spoon - http://spoon.gforge.inria.fr/
 *
 * This software is governed by the CeCILL-C License under French law and
 * abiding by the rules of distribution of free software. You can use, modify
 * and/or redistribute the software under the terms of the CeCILL-C license as
 * circulated by CEA, CNRS and INRIA at http://www.cecill.info.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the CeCILL-C License for more details.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-C license and that you accept its terms.
 */
package spoon.reflect.declaration;

import spoon.processing.FactoryAccessor;
import spoon.reflect.code.CtComment;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.reference.CtReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.CtVisitable;
import spoon.reflect.visitor.Filter;
import spoon.reflect.visitor.ReferenceFilter;
import spoon.reflect.visitor.Root;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

/**
 * This interface is the root interface for the metamodel elements (any program
 * element).
 */
@Root
public interface CtElement extends FactoryAccessor, CtVisitable, Cloneable {

	/**
	 * Searches for an annotation (proxy) of the given class that annotates the
	 * current element.
	 *
	 * <p>
	 * NOTE: before using an annotation proxy, you have to make sure that all
	 * the types referenced by the annotation have been compiled and are in the
	 * classpath so that accessed values can be converted into the actual types.
	 * Otherwise, use {@link #getAnnotation(CtTypeReference)}.
	 *
	 * @param <A>
	 * 		the annotation's type
	 * @param annotationType
	 * 		the annotation's class
	 * @return if found, returns a proxy for this annotation
	 */
	<A extends Annotation> A getAnnotation(Class<A> annotationType);

	/**
	 * Gets the annotation element for a given annotation type.
	 *
	 * @param annotationType
	 * 		the annotation type
	 * @return the annotation if this element is annotated by one annotation of
	 * the given type
	 */
	<A extends Annotation> CtAnnotation<A> getAnnotation(
			CtTypeReference<A> annotationType);

	/**
	 * Returns the annotations that are present on this element.
	 *
	 * For sake of encapsulation, the returned list is unmodifiable.
	 */
	List<CtAnnotation<? extends Annotation>> getAnnotations();

	/**
	 * Returns the text of the documentation ("javadoc") comment of this
	 * element. The documentation is also accessible via {@link #getComments()}.
	 */
	String getDocComment();


	/**
	 * Gets the signature of the element.
	 * Has been introduced for CtMethod, see chapter "8.4.2 Method Signature" of the Java specification.
	 * Overtime, has been badly exploited.
	 *
 	 * Deprecated, will be moved in {@link CtExecutable}, in order to follow the Java specification
	 */
	@Deprecated
	String getSignature();

	/**
	 * Gets the position of this element in input source files
	 *
	 * @return Source file and line number of this element or null
	 */
	SourcePosition getPosition();

	/**
	 * Replaces this element by another one.
	 */
	void replace(CtElement element);

	/**
	 * Add an annotation for this element
	 *
	 * @param annotation
	 * @return <tt>true</tt> if this element changed as a result of the call
	 */
	<E extends CtElement> E addAnnotation(CtAnnotation<? extends Annotation> annotation);

	/**
	 * Remove an annotation for this element
	 *
	 * @param annotation
	 * @return <tt>true</tt> if this element changed as a result of the call
	 */
	boolean removeAnnotation(CtAnnotation<? extends Annotation> annotation);

	/**
	 * Sets the text of the documentation ("javadoc") comment of this
	 * declaration. This API will set the content of the first javadoc
	 * {@link CtComment} or create a new  javadoc {@link CtComment} if
	 * no javadoc {@link CtComment} is available on this object.
	 */
	<E extends CtElement> E setDocComment(String docComment);

	/**
	 * Sets the position in the Java source file. Note that this information is
	 * used to feed the line numbers in the generated bytecode if any (which is
	 * useful for debugging).
	 *
	 * @param position
	 * 		of this element in the input source files
	 */
	<E extends CtElement> E setPosition(SourcePosition position);

	/**
	 * Gets the child elements annotated with the given annotation type's
	 * instances.
	 *
	 * @param <E>
	 * 		the element's type
	 * @param annotationType
	 * 		the annotation type
	 * @return all the child elements annotated with an instance of the given
	 * annotation type
	 */
	<E extends CtElement> List<E> getAnnotatedChildren(
			Class<? extends Annotation> annotationType);

	/**
	 * Returns true if this element is implicit and automatically added by the
	 * Java compiler.
	 */
	boolean isImplicit();

	/**
	 * Sets this element to be implicit (will not be printed).
	 */
	<E extends CtElement> E setImplicit(boolean b);

	/**
	 * Calculates and returns the set of all the types referenced by this
	 * element (and sub-elements in the AST).
	 */
	Set<CtTypeReference<?>> getReferencedTypes();

	/**
	 * Returns all the children elements recursively matching the filter.
	 * If the receiver (this) matches the filter, it is also returned
	 */
	<E extends CtElement> List<E> getElements(Filter<E> filter);

	/**
	 * @param filter
	 * @return
	 */
	<T extends CtReference> List<T> getReferences(ReferenceFilter<T> filter);

	/**
	 * Sets the position of this element and all its children element. Note that
	 * this information is used to feed the line numbers in the generated
	 * bytecode if any (which is useful for debugging).
	 *
	 * @param position
	 * 		of this element and all children in the input source file
	 */
	<E extends CtElement> E setPositions(SourcePosition position);

	/**
	 * Sets the annotations for this element.
	 */
	<E extends CtElement> E setAnnotations(List<CtAnnotation<? extends Annotation>> annotation);

	/**
	 * Gets the parent of current reference.
	 *
	 * @throws ParentNotInitializedException
	 * 		when the parent of this element is not initialized
	 */
	CtElement getParent() throws ParentNotInitializedException;

	/**
	 * Gets the first parent that matches the given type.
	 */
	<P extends CtElement> P getParent(Class<P> parentType) throws ParentNotInitializedException;

	/**
	 * Gets the first parent that matches the filter.
	 * If the receiver (this) matches the filter, it is also returned
	 */
	<E extends CtElement> E getParent(Filter<E> filter) throws ParentNotInitializedException;

	/**
	 * Manually sets the parent element of the current element.
	 *
	 * @param parent
	 * 		parent reference.
	 */
	<E extends CtElement> E setParent(E parent);

	/**
	 * Tells if this parent has been initialized.
	 */
	boolean isParentInitialized();

	/**
	 * Tells if the given element is a direct or indirect parent.
	 */
	boolean hasParent(CtElement candidate) throws ParentNotInitializedException;

	/**
	 * Calculates and sets all the parents below this element. This function can
	 * be called to check and fix parents after manipulating the model.
	 */
	void updateAllParentsBelow();

	/*
	 * Deletes the element. For instance, delete a statement from its containing block. Warning: it may result in an incorrect AST, use at your own risk.
	 */
	void delete();

	/*
	 * Saves metadata inside an Element.
	 */
	<E extends CtElement> E putMetadata(String key, Object val);

	/*
	 * Retrieves metadata stored in an element. Returns null if it does not exist.
	 */
	Object getMetadata(String key);

	/*
	 * Returns the metadata keys stored in an element.
	 */
	Set<String> getMetadataKeys();

	/**
	 * Set the comment list
	 */
	<E extends CtElement> E setComments(List<CtComment> comments);

	/**
	 * The list of comments
	 * @return the list of comment
	 */
	List<CtComment> getComments();

	/**
	 * Add a comment to the current element
	 * <code>element.addComment(element.getFactory().Code().createComment("comment", CtComment.CommentType.INLINE)</code>
	 * @param comment the comment
	 */
	<E extends CtElement> E addComment(CtComment comment);

	/**
	 * Remove a comment
	 * @param comment the comment to remove
	 */
	<E extends CtElement> E removeComment(CtComment comment);

	/**
	 * Clone the element which calls this method in a new object.
	 */
	CtElement clone();
}
