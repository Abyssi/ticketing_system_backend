package com.isssr.ticketing_system.logger;

import com.isssr.ticketing_system.logger.entity.Record;
import com.isssr.ticketing_system.logger.utils.ObjSer;
import com.isssr.ticketing_system.logger.utils.ReflectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

@Service
public class RecordController {

    @Autowired
    private RecordDao recordDao;

    @Transactional
    public @NotNull Record createRecord(@NotNull Record record) {
        return recordDao.save(record);
    }

    public boolean deleteRecord(@NotNull Integer id) {
        if (!recordDao.existsById(id)) {
            return false;
        }
        recordDao.deleteById(id);
        return true;
    }

    public List<Record> getAllRecords() {
        return recordDao.findAll();
    }

    public Record findRecordById(@NotNull Integer id) {
        if (!recordDao.existsById(id)) {
            return null;
        }
        return recordDao.getOne(id);
    }

    public List<Record> getRecordsByTag(@NotNull String tag) {
        return recordDao.getRecordsByTag(tag);
    }

    public List<Record> getRecordsByAuthor(@NotNull String author) {
        return recordDao.getRecordsByAuthor(author);
    }

    public List<Record> getRecordsByObjectId(Object object) {
        String[] idParams = ReflectUtils.getIDParameters(object);
        String objectId = null;

        try {
            objectId = ObjSer.buildIDJson(object, idParams);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return recordDao.getRecordsByObjectId(objectId);
    }

    public List<Record> getRecordsByOperation(String opName) {
        return recordDao.getRecordsByOperationName(opName);
    }
}