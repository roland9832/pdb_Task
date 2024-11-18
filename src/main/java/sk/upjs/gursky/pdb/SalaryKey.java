package sk.upjs.gursky.pdb;

import sk.upjs.gursky.bplustree.BPKey;

import java.nio.ByteBuffer;

public class SalaryKey implements BPKey<SalaryKey> {
    private static final long serialVersionUID = 5369886458513251549L;
    private int key;

    public SalaryKey() {
    }

    public SalaryKey(int key) {
        this.key = key;
    }
    @Override
    public int getSize() {
        return 4;
    }
    @Override
    public void load(ByteBuffer bb) {
        bb.getInt();
    }
    @Override
    public void save(ByteBuffer bb) {
        bb.putInt(key);
    }
    @Override
    public int compareTo(SalaryKey salaryKey) {
        if(this.key < salaryKey.key){
            return -1;
        }
        if(this.key > salaryKey.key){
            return 1;
        }else{
            return 0;
        }
    }
}
