package com.isssr.ticketing_system.logger;

import com.isssr.ticketing_system.exception.PageableQueryException;
import com.isssr.ticketing_system.logger.entity.Record;
import com.isssr.ticketing_system.logger.utils.ObjSer;
import com.isssr.ticketing_system.logger.utils.ReflectUtils;
import com.isssr.ticketing_system.utils.PageableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
public class RecordService {

    @Autowired
    private RecordDao recordDao;

    @Autowired
    private PageableUtils pageableUtils;

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

    @Transactional
    public Page<Record> findAll(@NotNull Integer page, @Nullable Integer pageSize) throws PageableQueryException {
        Page<Record> retrievedPage = this.recordDao.findAll(pageableUtils.instantiatePageableObject(page, pageSize, null));

        if (page != 0 && page > retrievedPage.getTotalPages() - 1)
            throw new PageableQueryException("Page number higher than the maximum");

        return retrievedPage;
    }

    @Transactional
    public Optional<Record> findById(Integer id) {
        return this.recordDao.findById(id);
    }
}