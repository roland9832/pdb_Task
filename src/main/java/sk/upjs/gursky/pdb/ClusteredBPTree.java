package sk.upjs.gursky.pdb;

import sk.upjs.gursky.bplustree.BPKey;
import sk.upjs.gursky.bplustree.BPTree;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClusteredBPTree extends BPTree<PersonStringKey, PersonEntry> {
    public static final File INDEX_FILE = new File("person.tree");
    public static final File INPUT_DATA_FILE = new File("person.tab");


    public ClusteredBPTree() throws IOException {
        super(PersonEntry.class, INDEX_FILE); //index, subor na uloz stromu, subor na uloz metadat
    }

    public static ClusteredBPTree createOneByOne() throws IOException {
        long startTime = System.nanoTime();
        ClusteredBPTree tree = new ClusteredBPTree();
        tree.openNewFile();

        RandomAccessFile raf = new RandomAccessFile(INPUT_DATA_FILE, "r");

        FileChannel channel = raf.getChannel();
        ByteBuffer buffer = ByteBuffer.allocateDirect(4096);

        //citat subor budeme tolko krat kolko je v nom dat
        long filesize = INPUT_DATA_FILE.length();//kolko ma bajtov
        //kazdu stranku suboru si nacitame do buffera
        for (int offset = 0; offset < filesize; offset += 4096) { //pre kazdu stranku si zapamatam jej offset
            System.out.println("procesing page " + (offset / 4096));
            //precitame stranku
            buffer.clear();
            channel.read(buffer, offset); //kam sa zapise a od akeho offsetu cita
            buffer.rewind();//aby sme mohli citat dame sipku na zaciatok
            int numberOfRecords = buffer.getInt(); //precita 4 bajty
            for (int i = 0; i < numberOfRecords; i++) {
                PersonEntry entry = new PersonEntry();
                entry.load(buffer);
                tree.add(entry);//do stromu pridame entry - zapise sa na disk //po jednom tak ako sme to robili na tabuli

            }
        }
        channel.close();
        raf.close();
        System.out.println("index created in: " + (System.nanoTime() - startTime) / 1_000_000.0 + "ms");
        return tree;
    }


    public static ClusteredBPTree createByBulkLoading() throws IOException {
        long startTime = System.nanoTime();
        ClusteredBPTree tree = new ClusteredBPTree();
        tree.openNewFile();

        RandomAccessFile raf = new RandomAccessFile(INPUT_DATA_FILE, "r");

        FileChannel channel = raf.getChannel();
        ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
        //dalo by sa urobit najprv externe triedenie a potom z toho citat, my trz len do listu dame

        List<PersonEntry> entries = new ArrayList<>();
        long filesize = INPUT_DATA_FILE.length();


        for (int offset = 0; offset < filesize; offset += 4096) {
            System.out.println("procesing page " + (offset / 4096));

            buffer.clear();
            channel.read(buffer, offset);
            buffer.rewind();
            int numberOfRecords = buffer.getInt();
            for (int i = 0; i < numberOfRecords; i++) {
                PersonEntry entry = new PersonEntry();
                entry.load(buffer);
                //tree.add(entry);
                entries.add(entry);//ludi pchame neusporiadane do jedneho pola == je to cele v ramke

            }

        }

        Collections.sort(entries);
        tree.openAndBatchUpdate(entries.iterator(), entries.size()); //listy su plne na 100%
        channel.close();
        raf.close();
        System.out.println("clustered index created in: " + (System.nanoTime() - startTime) / 1_000_000.0 + "ms");

        return tree;

    }
}