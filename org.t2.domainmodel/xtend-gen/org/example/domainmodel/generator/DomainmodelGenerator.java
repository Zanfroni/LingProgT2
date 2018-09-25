package org.example.domainmodel.generator;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.AbstractGenerator;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.generator.IGeneratorContext;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.example.domainmodel.domainmodel.Entity;
import org.example.domainmodel.domainmodel.Feature;

@SuppressWarnings("all")
public class DomainmodelGenerator extends AbstractGenerator {
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  @Override
  public void doGenerate(final Resource resource, final IFileSystemAccess2 fsa, final IGeneratorContext context) {
    Iterable<Entity> _filter = Iterables.<Entity>filter(IteratorExtensions.<EObject>toIterable(resource.getAllContents()), Entity.class);
    for (final Entity e : _filter) {
      String _string = this._iQualifiedNameProvider.getFullyQualifiedName(e).toString("/");
      String _plus = (_string + ".sql");
      fsa.generateFile(_plus, this.compile(e));
    }
  }
  
  public CharSequence compile(final Entity e) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("CREATE TABLE ");
    String _name = e.getName();
    _builder.append(_name);
    _builder.append(" ");
    {
      Entity _superType = e.getSuperType();
      boolean _tripleNotEquals = (_superType != null);
      if (_tripleNotEquals) {
        QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(e.getSuperType());
        _builder.append(_fullyQualifiedName);
        _builder.append(" ");
      }
    }
    _builder.append("(");
    _builder.newLineIfNotEmpty();
    {
      EList<Feature> _features = e.getFeatures();
      for(final Feature f : _features) {
        _builder.append("    ");
        CharSequence _compile = this.compile(f);
        _builder.append(_compile, "    ");
        {
          EList<Feature> _features_1 = e.getFeatures();
          int _size = e.getFeatures().size();
          int _minus = (_size - 1);
          Feature _get = _features_1.get(_minus);
          boolean _notEquals = (!Objects.equal(_get, f));
          if (_notEquals) {
            _builder.append(",");
          }
        }
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append(") ");
    {
      Entity _superType_1 = e.getSuperType();
      boolean _tripleNotEquals_1 = (_superType_1 != null);
      if (_tripleNotEquals_1) {
        _builder.append("inherits (");
        String _name_1 = e.getSuperType().getName();
        _builder.append(_name_1);
        _builder.append(")");
      }
    }
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence compile(final Feature f) {
    StringConcatenation _builder = new StringConcatenation();
    String _name = f.getName();
    _builder.append(_name);
    _builder.append(" ");
    QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(f.getType());
    _builder.append(_fullyQualifiedName);
    _builder.append(" ");
    {
      String _not = f.getNot();
      boolean _tripleNotEquals = (_not != null);
      if (_tripleNotEquals) {
        _builder.append("not ");
      }
    }
    _builder.append("null ");
    {
      String _key = f.getKey();
      boolean _tripleNotEquals_1 = (_key != null);
      if (_tripleNotEquals_1) {
        String _lowerCase = f.getKey().toLowerCase();
        _builder.append(_lowerCase);
        _builder.append(" key");
      }
    }
    _builder.newLineIfNotEmpty();
    return _builder;
  }
}
