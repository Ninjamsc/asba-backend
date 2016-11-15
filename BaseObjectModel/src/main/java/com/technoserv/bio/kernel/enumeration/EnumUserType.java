package com.technoserv.bio.kernel.enumeration;

import org.hibernate.HibernateException;
import org.hibernate.type.NullableType;
import org.hibernate.type.TypeFactory;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by sergey on 16.11.2016.
 */
public class EnumUserType implements UserType, ParameterizedType {

    /** Full class name of the enumerated type. */
    private static final String ENUM_CLASS_PARAM = "enumClass";

    /**
     * Name of parameter which holds the name of the method to retrieve value that will be stored in the persistent
     * storage.
     */
    private static final String IDENTIFIER_METHOD_PARAM = "identifierMethod";

    /**
     * Name of parameter which holds the name of the method to retrive value of the enumerated type according to value
     * in the persistent storage.
     */
    private static final String VALUE_OF_METHOD_PARAM = "valueOfMethod";

    /** Default value for the {@link #IDENTIFIER_METHOD_PARAM} parameter. */
    private static final String DEFAULT_IDENTIFIER_METHOD_NAME = "name";

    /** Default value for the {@link #VALUE_OF_METHOD_PARAM} parameter. */
    private static final String DEFAULT_VALUE_OF_METHOD_NAME = "valueOf";

    /** The {@link java.lang.Class} instance of the enumerated type. */
    private Class<? extends Enum> enumClass;

    /** Method identified by {@link #IDENTIFIER_METHOD_PARAM} parameter. */
    private Method identifierMethod;

    /** Method identified by {@link #VALUE_OF_METHOD_PARAM} parameter. */
    private Method valueOfMethod;

    /** Hibernate type to map type of return value of the identifier method. */
    private NullableType type;

    /** SQL type codes for the columns mapped by this type. */
    private int[] sqlTypes;

    /** Gets called by Hibernate to pass the configured type parameters. */
    public void setParameterValues(Properties parameters) {
        String enumClassName = parameters.getProperty(ENUM_CLASS_PARAM);
        try {
            enumClass = Class.forName(enumClassName).asSubclass(Enum.class);
        } catch (ClassNotFoundException e) {
            throw new HibernateException("Enum class not found", e);
        }

        String identifierMethodName = parameters.getProperty(IDENTIFIER_METHOD_PARAM, DEFAULT_IDENTIFIER_METHOD_NAME);

        Class<?> identifierType = null;
        try {
            identifierMethod = enumClass.getMethod(identifierMethodName);
            identifierType = identifierMethod.getReturnType();
        } catch (Exception e) {
            throw new HibernateException("Failed to obtain identifier method", e);
        }

        type = (NullableType) TypeFactory.basic(identifierType.getName());

        if (type == null) {
            throw new HibernateException("Unsupported identifier type " + identifierType.getName());
        }

        sqlTypes = new int[]{type.sqlType()};

        String valueOfMethodName = parameters.getProperty(VALUE_OF_METHOD_PARAM, DEFAULT_VALUE_OF_METHOD_NAME);

        try {
            valueOfMethod = enumClass.getMethod(valueOfMethodName, identifierType);
        } catch (Exception e) {
            throw new HibernateException("Failed to obtain valueOf method", e);
        }
    }

    /**
     * Gets returned {@link java.lang.Class} instance of the enumerated type.
     *
     * @return the {@link java.lang.Class} instance of the enumerated type
     */
    public Class<? extends Enum> returnedClass() {
        return enumClass;
    }

    /**
     * Retrieve an instance of the mapped class from a JDBC resultset.
     *
     * @param rs a JDBC result set
     * @param names the column names
     * @param owner the containing entity
     *
     * @return an instance of the mapped class from a JDBC resultset
     *
     * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet,String[],Object)
     */
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        Object identifier = type.get(rs, names[0]);
        if (identifier == null) {
            return null;
        }

        try {
            return valueOfMethod.invoke(enumClass, identifier);
        } catch (Exception e) {
            throw new HibernateException(
                    "Exception while invoking valueOf method '" + valueOfMethod.getName() + "' of " +
                            "enumeration class '" + enumClass + "'", e);
        }
    }

    /**
     * Write an instance of the mapped class to a prepared statement.
     *
     * @param st a JDBC prepared statement
     * @param value the object to write
     * @param index statement parameter index
     *
     * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement,Object,int)
     */
    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
        try {
            if (value == null) {
                st.setNull(index, type.sqlType());
            } else {
                Object identifier = identifierMethod.invoke(value);
                type.set(st, identifier, index);
            }
        } catch (Exception e) {
            throw new HibernateException(
                    "Exception while invoking identifierMethod '" + identifierMethod.getName() + "' of " +
                            "enumeration class '" + enumClass + "'", e);
        }
    }

    /**
     * Return the SQL type codes for the columns mapped by this type. The codes are defined on <tt>java.sql.Types</tt>.
     *
     * @return int[] the SQL type codes for the columns mapped by this type
     *
     * @see java.sql.Types
     * @see org.hibernate.usertype.UserType#sqlTypes()
     */
    public int[] sqlTypes() {
        return sqlTypes;
    }

    /**
     * Reconstruct an object from the cacheable representation.
     *
     * @param cached the object to be cached
     * @param owner the owner of the cached object
     *
     * @return a reconstructed object from the cachable representation
     *
     * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable,Object)
     */
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    /**
     * Return a deep copy of the persistent state, stopping at entities and at collections.
     *
     * @param value the object to be cloned, which may be null
     *
     * @return Object a deep copy of the persistent state
     *
     * @see org.hibernate.usertype.UserType#deepCopy(Object)
     */
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    /**
     * Transform the object into its cacheable representation.
     *
     * @param value the object to be cached
     *
     * @return a cacheable representation of the object
     *
     * @see org.hibernate.usertype.UserType#disassemble(Object)
     */
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    /**
     * Compare two instances of the class mapped by this type for persistence &quot;equality&quot;.
     *
     * @param x the first object
     * @param y the second object
     *
     * @return the <code>boolean</code> result of comparison
     *
     * @see org.hibernate.usertype.UserType#equals(Object,Object)
     */
    public boolean equals(Object x, Object y) throws HibernateException {
        return x == y;
    }

    /**
     * Get a hashcode for the instance, consistent with persistence &quot;equality&quot;
     *
     * @return the hashcode for the instance
     *
     * @see org.hibernate.usertype.UserType#hashCode(Object)
     */
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    /**
     * Identifies whether objects of this type are mutable.
     *
     * @return boolean value of <code>true</code> if objects of this type are mutable and <code>false</code> otherwise.
     *
     * @see org.hibernate.usertype.UserType#isMutable()
     */
    public boolean isMutable() {
        return false;
    }


    /**
     * During merge, replace the existing (target) value in the entity we are merging to with a new (original) value
     * from the detached entity we are merging.
     *
     * @param original the value from the detached entity being merged
     * @param target the value in the managed entity
     *
     * @return the value to be merged
     *
     * @see org.hibernate.usertype.UserType#replace(Object,Object,Object)
     */
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}
