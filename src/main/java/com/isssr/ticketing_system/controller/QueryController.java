package com.isssr.ticketing_system.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.exception.EntityNotFoundException;
import com.isssr.ticketing_system.exception.PageableQueryException;
import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.logger.aspect.LogOperation;
import com.isssr.ticketing_system.model.TicketPriority;
import com.isssr.ticketing_system.model.auto_generated.db_metadata.Column;
import com.isssr.ticketing_system.model.auto_generated.db_metadata.Table;
import com.isssr.ticketing_system.model.auto_generated.temporary.DataBaseTimeQuery;
import com.isssr.ticketing_system.model.db_connection.DBConnectionInfo;
import com.isssr.ticketing_system.response_entity.*;
import com.isssr.ticketing_system.service.DBConnectionInfoService;
import com.isssr.ticketing_system.service.TicketPriorityService;
import com.isssr.ticketing_system.service.auto_generated.AutoGeneratedTicketService;
import com.isssr.ticketing_system.service.auto_generated.QueryService;
import com.isssr.ticketing_system.service.db_metadata.DBMetadataExtractor;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Validated
@RestController
@RequestMapping(path = "/api/v1/queries")
public class QueryController {

    @Autowired
    private AutoGeneratedTicketService autoGeneratedTicketService;

    @Autowired
    private QueryService queryService;

    @Autowired
    private TicketPriorityService ticketPriorityService;

    @Autowired
    private DBConnectionInfoService dbConnectionInfoService;

    @Autowired
    private DBMetadataExtractor dbMetadataExtractor;

    @JsonView(JsonViews.Basic.class)
    @RequestMapping(path = "metadata", method = RequestMethod.GET)
     @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity metadata(@AuthenticationPrincipal Principal principal) {

        Iterable<TicketPriority> priorities = this.ticketPriorityService.findAll();

        Iterable<DBConnectionInfo> dbConnectionInfo = this.dbConnectionInfoService.findAll();

        return new HashMapResponseEntityBuilder(HttpStatus.OK)
                .set("priorities", StreamSupport.stream(priorities.spliterator(), false).collect(Collectors.toList()))
                .set("dbConnectionInfo", StreamSupport.stream(dbConnectionInfo.spliterator(), false).collect(Collectors.toList()))
                .build();
    }

    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity create(@RequestBody DataBaseTimeQuery dataBaseTimeQuery) {

        //try to store new query
        //DataBaseTimeQuery query = queryService.create(dataBaseTimeQuery);

        dataBaseTimeQuery.setQueryPriority(this.ticketPriorityService.findById(dataBaseTimeQuery.getQueryPriority().getId()).get());

        //save query
        DataBaseTimeQuery query = this.queryService.create(dataBaseTimeQuery);

        // try to create new query
        try {
            autoGeneratedTicketService.activateQuery(query);
            this.queryService.updateOne(query.getId(), query);
        } catch (ParseException | SchedulerException e) {

            //TODO generate ticket here

            e.printStackTrace();

            return CommonResponseEntity.UnprocessableEntityResponseEntity("NOT CREATED BECAUSE " + e.getMessage());
        } catch (EntityNotFoundException | UpdateException e) {

            //TODO generate ticket here

            e.printStackTrace();

            return CommonResponseEntity.BaseResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "NOT CREATED BECAUSE " + e.getMessage());

        }


        return CommonResponseEntity.OkResponseEntity("CREATED");
    }

    @RequestMapping(value = "{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity update(@PathVariable Long id, @RequestBody DataBaseTimeQuery dataBaseTimeQuery) {

        try {

            this.queryService.updateOne(id, dataBaseTimeQuery);

        } catch (UpdateException e) {

            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());

        } catch (EntityNotFoundException e) {

            return CommonResponseEntity.NotFoundResponseEntity(e.getMessage());

        } catch (ParseException | SchedulerException e) {

            return CommonResponseEntity.BaseResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

        }

        return CommonResponseEntity.OkResponseEntity("UPDATED");

    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity delete(@PathVariable Long id) {

        try {

            DataBaseTimeQuery dataBaseTimeQuery = this.queryService.findById(id);

            //check if query is active
            if (dataBaseTimeQuery.isActive()) {

                //remove query from task scheduler, if something went wrong stop deleting
                if (!this.queryService.disableQuery(dataBaseTimeQuery)) {

                    return CommonResponseEntity.BaseResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong during query deleting");

                }
            }

            this.queryService.deleteById(id);

            return CommonResponseEntity.OkResponseEntity("DELETED");

        } catch (EntityNotFoundException e) {

            return CommonResponseEntity.NotFoundResponseEntity( e.getMessage());

        } catch (SchedulerException e) {

            return CommonResponseEntity.BaseResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

        }
    }

    @JsonView(JsonViews.Basic.class)
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity getAllNotDeletedPaginated(@RequestParam(name = "page") Integer page, @RequestParam(name = "pageSize", required = false) Integer pageSize) {

        Page<DataBaseTimeQuery> dataBaseTimeQueryPage;
        try {
            dataBaseTimeQueryPage = queryService.findAllNotDeleted(page, pageSize);
        } catch (PageableQueryException e) {
            return CommonResponseEntity.BadRequestResponseEntity(e.getMessage());
        }
        return new PageResponseEntityBuilder(dataBaseTimeQueryPage)
                .setStatus(HttpStatus.OK)
                .build();
    }

    @JsonView(JsonViews.Detailed.class)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity getAllNotDeletedPaginated(@PathVariable Long id) {

        try {

            DataBaseTimeQuery dataBaseTimeQuery = this.queryService.findById(id);

            return new ResponseEntityBuilder<>(dataBaseTimeQuery).setStatus(HttpStatus.OK).build();

        } catch (EntityNotFoundException e) {

            return CommonResponseEntity.NotFoundResponseEntity(e.getMessage());

        }
    }

    @RequestMapping(value = "tables", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity getTablesMetadata(@RequestBody DBConnectionInfo dbConnectionInfo) {

        try {

            List<Table> tables = this.dbMetadataExtractor.getTableMetadata(
                    dbConnectionInfo.getUrl(),
                    dbConnectionInfo.getUsername(),
                    dbConnectionInfo.getPassword()
            );

            return new ResponseEntity<List<Table>>(tables, HttpStatus.OK);

        } catch (SQLException e) {

            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @RequestMapping(value = "tables/{tableName}/columns", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity getTableColumnsMetadata(@PathVariable String tableName, @RequestBody DBConnectionInfo dbConnectionInfo) {

        try {

            List<Column> columns = this.dbMetadataExtractor.getTableColumns(
                    tableName,
                    dbConnectionInfo.getUrl(),
                    dbConnectionInfo.getUsername(),
                    dbConnectionInfo.getPassword()
            );

            return new ResponseEntity<List<Column>>(columns, HttpStatus.OK);

        } catch (SQLException e) {

            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @RequestMapping(value = "disable/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity disableQuery(@PathVariable() Long id) {

        try {

            DataBaseTimeQuery dataBaseTimeQuery = this.queryService.findById(id);

            boolean disabled = this.queryService.disableQuery(dataBaseTimeQuery);

            if (disabled) {

                return CommonResponseEntity.OkResponseEntity("Query disabled");

            } else {

                return CommonResponseEntity.BadRequestResponseEntity("Something went wrong during query disabling");

            }

        } catch (EntityNotFoundException e) {

            return CommonResponseEntity.NotFoundResponseEntity( e.getMessage());

        } catch (SchedulerException e) {

            return CommonResponseEntity.BaseResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @RequestMapping(value = "activate/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity activateQuery(@PathVariable() Long id) {

        try {

            DataBaseTimeQuery dataBaseTimeQuery = this.queryService.findById(id);

            boolean activate = this.queryService.activateQuery(dataBaseTimeQuery);

            if (activate) {

                return CommonResponseEntity.OkResponseEntity("Query activated");

            } else {

                return CommonResponseEntity.BadRequestResponseEntity("Something went wrong during query activating");

            }

        } catch (EntityNotFoundException e) {

            return CommonResponseEntity.NotFoundResponseEntity( e.getMessage());

        } catch (SchedulerException | ParseException e) {

            return CommonResponseEntity.BaseResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

        }

    }
}
