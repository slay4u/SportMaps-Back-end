package spring.app.modules.commons.constant;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseConstType implements UserType<BaseConst> {
    @Override
    public int getSqlType() {
        return 0;
    }

    @Override
    public Class<BaseConst> returnedClass() {
        return null;
    }

    @Override
    public boolean equals(BaseConst baseConst, BaseConst j1) {
        return false;
    }

    @Override
    public int hashCode(BaseConst baseConst) {
        return 0;
    }

    @Override
    public BaseConst nullSafeGet(ResultSet resultSet, int i, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws SQLException {
        return null;
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, BaseConst baseConst, int i, SharedSessionContractImplementor sharedSessionContractImplementor) throws SQLException {

    }

    @Override
    public BaseConst deepCopy(BaseConst baseConst) {
        return null;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(BaseConst baseConst) {
        return null;
    }

    @Override
    public BaseConst assemble(Serializable serializable, Object o) {
        return null;
    }

    @Override
    public BaseConst replace(BaseConst baseConst, BaseConst j1, Object o) {
        return null;
    }
}
