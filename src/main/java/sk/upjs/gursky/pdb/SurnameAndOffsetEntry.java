package sk.upjs.gursky.pdb;

import sk.upjs.gursky.bplustree.BPObject;

import java.nio.ByteBuffer;

//objekt kt je v listoch neklastroaneho stromu
public class SurnameAndOffsetEntry implements BPObject<PersonStringKey, SurnameAndOffsetEntry> {

    private static final long serialVersionUID = -1185657683261039704L;

    String surname;
    public long offset; //odkaz na miesto v subore, long lebo moze byt subor vacsi ako 4GB

    public SurnameAndOffsetEntry() {
    }

    public SurnameAndOffsetEntry(String surname, long offset) {
        this.surname = surname;
        this.offset = offset;
    }

    @Override
    public void load(ByteBuffer bb) {//najprv precitaj priezvisko, potom offset
        char[] data = new char[10];

        for (int i = 0; i < 10; i++) {//znak po znaku citame priezvisko
            data[i] = bb.getChar();
        }
        surname = new String(data);
        offset = bb.getLong(); //ukradneme si offset

    }

    @Override
    public void save(ByteBuffer bb) {
        for (int i = 0; i < 10; i++) {//znak po znaku citame priezvisko
            bb.putChar(surname.charAt(i));
        }
        bb.putLong(offset);

    }

    @Override
    public int getSize() {
        //jeden znak 2 bajty, 10 znakov, priezvisko 20 bajtov
        //long ma 8 bajtov
        return 28;
    }

    @Override
    public PersonStringKey getKey() {
        return new PersonStringKey(surname);
    }

    @Override
    public int compareTo(SurnameAndOffsetEntry o) {
        return this.surname.compareTo(o.surname);
    }

    @Override
    public String toString() {
        return "SurnameAndOffsetEntry[" +
                "surname='" + surname + '\'' +
                ", offset=" + offset +
                ']';
    }
}
