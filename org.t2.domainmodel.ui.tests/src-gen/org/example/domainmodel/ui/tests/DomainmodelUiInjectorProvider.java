/*
 * generated by Xtext 2.12.0
 */
package org.example.domainmodel.ui.tests;

import com.google.inject.Injector;
import org.eclipse.xtext.testing.IInjectorProvider;
import org.t2.domainmodel.ui.internal.DomainmodelActivator;

public class DomainmodelUiInjectorProvider implements IInjectorProvider {

	@Override
	public Injector getInjector() {
		return DomainmodelActivator.getInstance().getInjector("org.example.domainmodel.Domainmodel");
	}

}
