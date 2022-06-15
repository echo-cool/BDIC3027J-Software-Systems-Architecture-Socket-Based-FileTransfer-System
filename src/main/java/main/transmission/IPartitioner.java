package main.transmission;

import main.file.AbstractPartitioner;

public interface IPartitioner {
    static AbstractPartitioner.SegmentedData partition(String data, int taskID, AbstractPartitioner.PartitionListener listener) {
        return null;
    }
}
