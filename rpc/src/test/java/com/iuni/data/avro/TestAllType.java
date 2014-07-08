package com.iuni.data.avro;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
@org.apache.avro.specific.AvroGenerated
public class TestAllType extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
    public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse(
            "{\"type\":\"record\",\"name\":\"Test\",\"namespace\":\"com.iuni.data.avro.common\",\"fields\":" +
                    "[{\"name\":\"stringVar\",\"type\":\"string\"}," +
                    "{\"name\":\"bytesVar\",\"type\":[\"bytes\",\"null\"]}," +
                    "{\"name\":\"booleanVar\",\"type\":\"boolean\"}," +
                    "{\"name\":\"intVar\",\"type\":\"int\",\"order\":\"descending\"}," +
                    "{\"name\":\"longVar\",\"type\":[\"long\",\"null\"]}," +
                    "{\"name\":\"floatVar\",\"type\":\"float\"}," +
                    "{\"name\":\"doubleVar\",\"type\":\"double\"}," +
                    "{\"name\":\"enumVar\",\"type\":{\"type\":\"enum\",\"name\":\"Suit\",\"symbols\":[\"SPADES\",\"HEARTS\",\"DIAMONDS\",\"CLUBS\"]}}," +
                    "{\"name\":\"strArrayVar\",\"type\":{\"type\":\"array\",\"items\":\"string\"}}," +
                    "{\"name\":\"intArrayVar\",\"type\":{\"type\":\"array\",\"items\":\"int\"}}," +
                    "{\"name\":\"mapVar\",\"type\":{\"type\":\"map\",\"values\":\"long\"}}," +
                    "{\"name\":\"fixedVar\",\"type\":{\"type\":\"fixed\",\"name\":\"Md5\",\"size\":16}}]}");

    public static org.apache.avro.Schema getClassSchema() {
        return SCHEMA$;
    }

    @Deprecated
    public java.lang.CharSequence stringVar;
    @Deprecated
    public java.nio.ByteBuffer bytesVar;
    @Deprecated
    public boolean booleanVar;
    @Deprecated
    public int intVar;
    @Deprecated
    public java.lang.Long longVar;
    @Deprecated
    public float floatVar;
    @Deprecated
    public double doubleVar;
    @Deprecated
    public Suit enumVar;
    @Deprecated
    public java.util.List<java.lang.CharSequence> strArrayVar;
    @Deprecated
    public java.util.List<java.lang.Integer> intArrayVar;
    @Deprecated
    public java.util.Map<java.lang.CharSequence, java.lang.Long> mapVar;
    @Deprecated
    public Md5 fixedVar;

    public TestAllType() {
    }

    public TestAllType(java.lang.CharSequence stringVar, java.nio.ByteBuffer bytesVar, java.lang.Boolean booleanVar, java.lang.Integer intVar, java.lang.Long longVar, java.lang.Float floatVar, java.lang.Double doubleVar, Suit enumVar, java.util.List<java.lang.CharSequence> strArrayVar, java.util.List<java.lang.Integer> intArrayVar, java.util.Map<java.lang.CharSequence, java.lang.Long> mapVar, Md5 fixedVar) {
        this.stringVar = stringVar;
        this.bytesVar = bytesVar;
        this.booleanVar = booleanVar;
        this.intVar = intVar;
        this.longVar = longVar;
        this.floatVar = floatVar;
        this.doubleVar = doubleVar;
        this.enumVar = enumVar;
        this.strArrayVar = strArrayVar;
        this.intArrayVar = intArrayVar;
        this.mapVar = mapVar;
        this.fixedVar = fixedVar;
    }

    public org.apache.avro.Schema getSchema() {
        return SCHEMA$;
    }

    // Used by DatumWriter.  Applications should not call.
    public java.lang.Object get(int field$) {
        switch (field$) {
            case 0:
                return stringVar;
            case 1:
                return bytesVar;
            case 2:
                return booleanVar;
            case 3:
                return intVar;
            case 4:
                return longVar;
            case 5:
                return floatVar;
            case 6:
                return doubleVar;
            case 7:
                return enumVar;
            case 8:
                return strArrayVar;
            case 9:
                return intArrayVar;
            case 10:
                return mapVar;
            case 11:
                return fixedVar;
            default:
                throw new org.apache.avro.AvroRuntimeException("Bad index");
        }
    }

    // Used by DatumReader.  Applications should not call.
    public void put(int field$, java.lang.Object value$) {
        switch (field$) {
            case 0:
                stringVar = (java.lang.CharSequence) value$;
                break;
            case 1:
                bytesVar = (java.nio.ByteBuffer) value$;
                break;
            case 2:
                booleanVar = (java.lang.Boolean) value$;
                break;
            case 3:
                intVar = (java.lang.Integer) value$;
                break;
            case 4:
                longVar = (java.lang.Long) value$;
                break;
            case 5:
                floatVar = (java.lang.Float) value$;
                break;
            case 6:
                doubleVar = (java.lang.Double) value$;
                break;
            case 7:
                enumVar = (Suit) value$;
                break;
            case 8:
                strArrayVar = (java.util.List<java.lang.CharSequence>) value$;
                break;
            case 9:
                intArrayVar = (java.util.List<java.lang.Integer>) value$;
                break;
            case 10:
                mapVar = (java.util.Map<java.lang.CharSequence, java.lang.Long>) value$;
                break;
            case 11:
                fixedVar = (Md5) value$;
                break;
            default:
                throw new org.apache.avro.AvroRuntimeException("Bad index");
        }
    }

    public java.lang.CharSequence getStringVar() {
        return stringVar;
    }

    public void setStringVar(java.lang.CharSequence value) {
        this.stringVar = value;
    }

    public java.nio.ByteBuffer getBytesVar() {
        return bytesVar;
    }

    public void setBytesVar(java.nio.ByteBuffer value) {
        this.bytesVar = value;
    }

    public java.lang.Boolean getBooleanVar() {
        return booleanVar;
    }

    public void setBooleanVar(java.lang.Boolean value) {
        this.booleanVar = value;
    }

    public java.lang.Integer getIntVar() {
        return intVar;
    }

    public void setIntVar(java.lang.Integer value) {
        this.intVar = value;
    }

    public java.lang.Long getLongVar() {
        return longVar;
    }

    public void setLongVar(java.lang.Long value) {
        this.longVar = value;
    }

    public java.lang.Float getFloatVar() {
        return floatVar;
    }

    public void setFloatVar(java.lang.Float value) {
        this.floatVar = value;
    }

    public java.lang.Double getDoubleVar() {
        return doubleVar;
    }

    public void setDoubleVar(java.lang.Double value) {
        this.doubleVar = value;
    }

    public Suit getEnumVar() {
        return enumVar;
    }

    public void setEnumVar(Suit value) {
        this.enumVar = value;
    }

    public java.util.List<java.lang.CharSequence> getStrArrayVar() {
        return strArrayVar;
    }

    public void setStrArrayVar(java.util.List<java.lang.CharSequence> value) {
        this.strArrayVar = value;
    }

    public java.util.List<java.lang.Integer> getIntArrayVar() {
        return intArrayVar;
    }

    public void setIntArrayVar(java.util.List<java.lang.Integer> value) {
        this.intArrayVar = value;
    }

    public java.util.Map<java.lang.CharSequence, java.lang.Long> getMapVar() {
        return mapVar;
    }

    public void setMapVar(java.util.Map<java.lang.CharSequence, java.lang.Long> value) {
        this.mapVar = value;
    }

    public Md5 getFixedVar() {
        return fixedVar;
    }

    public void setFixedVar(Md5 value) {
        this.fixedVar = value;
    }

    public static com.iuni.data.avro.TestAllType.Builder newBuilder() {
        return new com.iuni.data.avro.TestAllType.Builder();
    }

    public static com.iuni.data.avro.TestAllType.Builder newBuilder(com.iuni.data.avro.TestAllType.Builder other) {
        return new com.iuni.data.avro.TestAllType.Builder(other);
    }

    public static com.iuni.data.avro.TestAllType.Builder newBuilder(com.iuni.data.avro.TestAllType other) {
        return new com.iuni.data.avro.TestAllType.Builder(other);
    }

    public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<TestAllType>
            implements org.apache.avro.data.RecordBuilder<TestAllType> {
        private java.lang.CharSequence stringVar;
        private java.nio.ByteBuffer bytesVar;
        private boolean booleanVar;
        private int intVar;
        private java.lang.Long longVar;
        private float floatVar;
        private double doubleVar;
        private Suit enumVar;
        private java.util.List<java.lang.CharSequence> strArrayVar;
        private java.util.List<java.lang.Integer> intArrayVar;
        private java.util.Map<java.lang.CharSequence, java.lang.Long> mapVar;
        private Md5 fixedVar;

        private Builder() {
            super(com.iuni.data.avro.TestAllType.SCHEMA$);
        }

        private Builder(com.iuni.data.avro.TestAllType.Builder other) {
            super(other);
            if (isValidValue(fields()[0], other.stringVar)) {
                this.stringVar = data().deepCopy(fields()[0].schema(), other.stringVar);
                fieldSetFlags()[0] = true;
            }
            if (isValidValue(fields()[1], other.bytesVar)) {
                this.bytesVar = data().deepCopy(fields()[1].schema(), other.bytesVar);
                fieldSetFlags()[1] = true;
            }
            if (isValidValue(fields()[2], other.booleanVar)) {
                this.booleanVar = data().deepCopy(fields()[2].schema(), other.booleanVar);
                fieldSetFlags()[2] = true;
            }
            if (isValidValue(fields()[3], other.intVar)) {
                this.intVar = data().deepCopy(fields()[3].schema(), other.intVar);
                fieldSetFlags()[3] = true;
            }
            if (isValidValue(fields()[4], other.longVar)) {
                this.longVar = data().deepCopy(fields()[4].schema(), other.longVar);
                fieldSetFlags()[4] = true;
            }
            if (isValidValue(fields()[5], other.floatVar)) {
                this.floatVar = data().deepCopy(fields()[5].schema(), other.floatVar);
                fieldSetFlags()[5] = true;
            }
            if (isValidValue(fields()[6], other.doubleVar)) {
                this.doubleVar = data().deepCopy(fields()[6].schema(), other.doubleVar);
                fieldSetFlags()[6] = true;
            }
            if (isValidValue(fields()[7], other.enumVar)) {
                this.enumVar = data().deepCopy(fields()[7].schema(), other.enumVar);
                fieldSetFlags()[7] = true;
            }
            if (isValidValue(fields()[8], other.strArrayVar)) {
                this.strArrayVar = data().deepCopy(fields()[8].schema(), other.strArrayVar);
                fieldSetFlags()[8] = true;
            }
            if (isValidValue(fields()[9], other.intArrayVar)) {
                this.intArrayVar = data().deepCopy(fields()[9].schema(), other.intArrayVar);
                fieldSetFlags()[9] = true;
            }
            if (isValidValue(fields()[10], other.mapVar)) {
                this.mapVar = data().deepCopy(fields()[10].schema(), other.mapVar);
                fieldSetFlags()[10] = true;
            }
            if (isValidValue(fields()[11], other.fixedVar)) {
                this.fixedVar = data().deepCopy(fields()[11].schema(), other.fixedVar);
                fieldSetFlags()[11] = true;
            }
        }

        private Builder(com.iuni.data.avro.TestAllType other) {
            super(com.iuni.data.avro.TestAllType.SCHEMA$);
            if (isValidValue(fields()[0], other.stringVar)) {
                this.stringVar = data().deepCopy(fields()[0].schema(), other.stringVar);
                fieldSetFlags()[0] = true;
            }
            if (isValidValue(fields()[1], other.bytesVar)) {
                this.bytesVar = data().deepCopy(fields()[1].schema(), other.bytesVar);
                fieldSetFlags()[1] = true;
            }
            if (isValidValue(fields()[2], other.booleanVar)) {
                this.booleanVar = data().deepCopy(fields()[2].schema(), other.booleanVar);
                fieldSetFlags()[2] = true;
            }
            if (isValidValue(fields()[3], other.intVar)) {
                this.intVar = data().deepCopy(fields()[3].schema(), other.intVar);
                fieldSetFlags()[3] = true;
            }
            if (isValidValue(fields()[4], other.longVar)) {
                this.longVar = data().deepCopy(fields()[4].schema(), other.longVar);
                fieldSetFlags()[4] = true;
            }
            if (isValidValue(fields()[5], other.floatVar)) {
                this.floatVar = data().deepCopy(fields()[5].schema(), other.floatVar);
                fieldSetFlags()[5] = true;
            }
            if (isValidValue(fields()[6], other.doubleVar)) {
                this.doubleVar = data().deepCopy(fields()[6].schema(), other.doubleVar);
                fieldSetFlags()[6] = true;
            }
            if (isValidValue(fields()[7], other.enumVar)) {
                this.enumVar = data().deepCopy(fields()[7].schema(), other.enumVar);
                fieldSetFlags()[7] = true;
            }
            if (isValidValue(fields()[8], other.strArrayVar)) {
                this.strArrayVar = data().deepCopy(fields()[8].schema(), other.strArrayVar);
                fieldSetFlags()[8] = true;
            }
            if (isValidValue(fields()[9], other.intArrayVar)) {
                this.intArrayVar = data().deepCopy(fields()[9].schema(), other.intArrayVar);
                fieldSetFlags()[9] = true;
            }
            if (isValidValue(fields()[10], other.mapVar)) {
                this.mapVar = data().deepCopy(fields()[10].schema(), other.mapVar);
                fieldSetFlags()[10] = true;
            }
            if (isValidValue(fields()[11], other.fixedVar)) {
                this.fixedVar = data().deepCopy(fields()[11].schema(), other.fixedVar);
                fieldSetFlags()[11] = true;
            }
        }

        public java.lang.CharSequence getStringVar() {
            return stringVar;
        }

        public com.iuni.data.avro.TestAllType.Builder setStringVar(java.lang.CharSequence value) {
            validate(fields()[0], value);
            this.stringVar = value;
            fieldSetFlags()[0] = true;
            return this;
        }

        public boolean hasStringVar() {
            return fieldSetFlags()[0];
        }

        public com.iuni.data.avro.TestAllType.Builder clearStringVar() {
            stringVar = null;
            fieldSetFlags()[0] = false;
            return this;
        }

        public java.nio.ByteBuffer getBytesVar() {
            return bytesVar;
        }

        public com.iuni.data.avro.TestAllType.Builder setBytesVar(java.nio.ByteBuffer value) {
            validate(fields()[1], value);
            this.bytesVar = value;
            fieldSetFlags()[1] = true;
            return this;
        }

        public boolean hasBytesVar() {
            return fieldSetFlags()[1];
        }

        public com.iuni.data.avro.TestAllType.Builder clearBytesVar() {
            bytesVar = null;
            fieldSetFlags()[1] = false;
            return this;
        }

        public java.lang.Boolean getBooleanVar() {
            return booleanVar;
        }

        public com.iuni.data.avro.TestAllType.Builder setBooleanVar(boolean value) {
            validate(fields()[2], value);
            this.booleanVar = value;
            fieldSetFlags()[2] = true;
            return this;
        }

        public boolean hasBooleanVar() {
            return fieldSetFlags()[2];
        }

        public com.iuni.data.avro.TestAllType.Builder clearBooleanVar() {
            fieldSetFlags()[2] = false;
            return this;
        }

        public java.lang.Integer getIntVar() {
            return intVar;
        }

        public com.iuni.data.avro.TestAllType.Builder setIntVar(int value) {
            validate(fields()[3], value);
            this.intVar = value;
            fieldSetFlags()[3] = true;
            return this;
        }

        public boolean hasIntVar() {
            return fieldSetFlags()[3];
        }

        public com.iuni.data.avro.TestAllType.Builder clearIntVar() {
            fieldSetFlags()[3] = false;
            return this;
        }

        public java.lang.Long getLongVar() {
            return longVar;
        }

        public com.iuni.data.avro.TestAllType.Builder setLongVar(java.lang.Long value) {
            validate(fields()[4], value);
            this.longVar = value;
            fieldSetFlags()[4] = true;
            return this;
        }

        public boolean hasLongVar() {
            return fieldSetFlags()[4];
        }

        public com.iuni.data.avro.TestAllType.Builder clearLongVar() {
            longVar = null;
            fieldSetFlags()[4] = false;
            return this;
        }

        public java.lang.Float getFloatVar() {
            return floatVar;
        }

        public com.iuni.data.avro.TestAllType.Builder setFloatVar(float value) {
            validate(fields()[5], value);
            this.floatVar = value;
            fieldSetFlags()[5] = true;
            return this;
        }

        public boolean hasFloatVar() {
            return fieldSetFlags()[5];
        }

        public com.iuni.data.avro.TestAllType.Builder clearFloatVar() {
            fieldSetFlags()[5] = false;
            return this;
        }

        public java.lang.Double getDoubleVar() {
            return doubleVar;
        }

        public com.iuni.data.avro.TestAllType.Builder setDoubleVar(double value) {
            validate(fields()[6], value);
            this.doubleVar = value;
            fieldSetFlags()[6] = true;
            return this;
        }

        public boolean hasDoubleVar() {
            return fieldSetFlags()[6];
        }

        public com.iuni.data.avro.TestAllType.Builder clearDoubleVar() {
            fieldSetFlags()[6] = false;
            return this;
        }

        public Suit getEnumVar() {
            return enumVar;
        }

        public com.iuni.data.avro.TestAllType.Builder setEnumVar(Suit value) {
            validate(fields()[7], value);
            this.enumVar = value;
            fieldSetFlags()[7] = true;
            return this;
        }

        public boolean hasEnumVar() {
            return fieldSetFlags()[7];
        }

        public com.iuni.data.avro.TestAllType.Builder clearEnumVar() {
            enumVar = null;
            fieldSetFlags()[7] = false;
            return this;
        }

        public java.util.List<java.lang.CharSequence> getStrArrayVar() {
            return strArrayVar;
        }

        public com.iuni.data.avro.TestAllType.Builder setStrArrayVar(java.util.List<java.lang.CharSequence> value) {
            validate(fields()[8], value);
            this.strArrayVar = value;
            fieldSetFlags()[8] = true;
            return this;
        }

        public boolean hasStrArrayVar() {
            return fieldSetFlags()[8];
        }

        public com.iuni.data.avro.TestAllType.Builder clearStrArrayVar() {
            strArrayVar = null;
            fieldSetFlags()[8] = false;
            return this;
        }

        public java.util.List<java.lang.Integer> getIntArrayVar() {
            return intArrayVar;
        }

        public com.iuni.data.avro.TestAllType.Builder setIntArrayVar(java.util.List<java.lang.Integer> value) {
            validate(fields()[9], value);
            this.intArrayVar = value;
            fieldSetFlags()[9] = true;
            return this;
        }

        public boolean hasIntArrayVar() {
            return fieldSetFlags()[9];
        }

        public com.iuni.data.avro.TestAllType.Builder clearIntArrayVar() {
            intArrayVar = null;
            fieldSetFlags()[9] = false;
            return this;
        }

        public java.util.Map<java.lang.CharSequence, java.lang.Long> getMapVar() {
            return mapVar;
        }

        public com.iuni.data.avro.TestAllType.Builder setMapVar(java.util.Map<java.lang.CharSequence, java.lang.Long> value) {
            validate(fields()[10], value);
            this.mapVar = value;
            fieldSetFlags()[10] = true;
            return this;
        }

        public boolean hasMapVar() {
            return fieldSetFlags()[10];
        }

        public com.iuni.data.avro.TestAllType.Builder clearMapVar() {
            mapVar = null;
            fieldSetFlags()[10] = false;
            return this;
        }

        public Md5 getFixedVar() {
            return fixedVar;
        }

        public com.iuni.data.avro.TestAllType.Builder setFixedVar(Md5 value) {
            validate(fields()[11], value);
            this.fixedVar = value;
            fieldSetFlags()[11] = true;
            return this;
        }

        public boolean hasFixedVar() {
            return fieldSetFlags()[11];
        }

        public com.iuni.data.avro.TestAllType.Builder clearFixedVar() {
            fixedVar = null;
            fieldSetFlags()[11] = false;
            return this;
        }

        @Override
        public TestAllType build() {
            try {
                TestAllType record = new TestAllType();
                record.stringVar = fieldSetFlags()[0] ? this.stringVar : (java.lang.CharSequence) defaultValue(fields()[0]);
                record.bytesVar = fieldSetFlags()[1] ? this.bytesVar : (java.nio.ByteBuffer) defaultValue(fields()[1]);
                record.booleanVar = fieldSetFlags()[2] ? this.booleanVar : (java.lang.Boolean) defaultValue(fields()[2]);
                record.intVar = fieldSetFlags()[3] ? this.intVar : (java.lang.Integer) defaultValue(fields()[3]);
                record.longVar = fieldSetFlags()[4] ? this.longVar : (java.lang.Long) defaultValue(fields()[4]);
                record.floatVar = fieldSetFlags()[5] ? this.floatVar : (java.lang.Float) defaultValue(fields()[5]);
                record.doubleVar = fieldSetFlags()[6] ? this.doubleVar : (java.lang.Double) defaultValue(fields()[6]);
                record.enumVar = fieldSetFlags()[7] ? this.enumVar : (Suit) defaultValue(fields()[7]);
                record.strArrayVar = fieldSetFlags()[8] ? this.strArrayVar : (java.util.List<java.lang.CharSequence>) defaultValue(fields()[8]);
                record.intArrayVar = fieldSetFlags()[9] ? this.intArrayVar : (java.util.List<java.lang.Integer>) defaultValue(fields()[9]);
                record.mapVar = fieldSetFlags()[10] ? this.mapVar : (java.util.Map<java.lang.CharSequence, java.lang.Long>) defaultValue(fields()[10]);
                record.fixedVar = fieldSetFlags()[11] ? this.fixedVar : (Md5) defaultValue(fields()[11]);
                return record;
            } catch (Exception e) {
                throw new org.apache.avro.AvroRuntimeException(e);
            }
        }
    }
}
