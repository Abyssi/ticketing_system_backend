package com.isssr.ticketing_system.logger;

import com.isssr.ticketing_system.logger.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

@Component
public class RecordReader {

    @Autowired
    private RecordService recordService;

    public void deleteRecord(@NotNull Integer id) {
        recordService.deleteRecord(id);
    }

    public List<Record> getAllRecords() {
        return recordService.getAllRecords();
    }

    public List<Record> getRecordsByTag(@NotNull String tag) {
        return recordService.getRecordsByTag(tag);
    }

    public List<Record> getRecordsByAuthor(@NotNull String author) {
        return recordService.getRecordsByAuthor(author);
    }

    public List<Record> getRecordsByOperation(@NotNull String opName) {
        return recordService.getRecordsByOperation(opName);
    }

    public List<Record> getRecordsByObjectId(@NotNull Object object) {
        return recordService.getRecordsByObjectId(object);
    }

    //TODO: query combinazione delle precedenti
}
