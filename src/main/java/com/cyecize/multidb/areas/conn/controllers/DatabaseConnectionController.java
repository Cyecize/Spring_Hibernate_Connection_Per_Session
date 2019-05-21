package com.cyecize.multidb.areas.conn.controllers;

import com.cyecize.multidb.areas.conn.bindingModels.DbCredentialsBindingModel;
import com.cyecize.multidb.areas.conn.enums.DatabaseProvider;
import com.cyecize.multidb.areas.conn.models.DbCredentials;
import com.cyecize.multidb.areas.conn.models.UserDbConnection;
import com.cyecize.multidb.areas.conn.services.ConnectionManager;
import com.cyecize.multidb.areas.conn.services.DbConnectionStorageService;
import com.cyecize.multidb.areas.conn.services.EntityMappingService;
import com.cyecize.multidb.areas.conn.services.SessionDbService;
import com.cyecize.multidb.constants.ValidationConstants;
import com.cyecize.multidb.controllers.BaseController;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/database")
@PreAuthorize("isAnonymous()")
public class DatabaseConnectionController extends BaseController {

    private final SessionDbService sessionDbConnection;

    private final DbConnectionStorageService connectionStorageService;

    private final EntityMappingService entityMappingService;

    private final ModelMapper modelMapper;

    @Autowired
    public DatabaseConnectionController(SessionDbService dbConnectionService, DbConnectionStorageService connectionStorageService,
                                        EntityMappingService entityMappingService, ModelMapper modelMapper) {
        this.sessionDbConnection = dbConnectionService;
        this.connectionStorageService = connectionStorageService;
        this.entityMappingService = entityMappingService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/connect")
    public ModelAndView databaseConnectionGetAction(Model model) {

        final UserDbConnection existingConnection = this.sessionDbConnection.getConnection();

        //Check if there is existing connection.
        if (existingConnection != null) {
            if (!model.asMap().containsKey(ValidationConstants.MODEL_NAME)) {
                model.addAttribute(ValidationConstants.MODEL_NAME, existingConnection.getCredentials());
            }
        }

        if (!model.asMap().containsKey(ValidationConstants.MODEL_NAME)) {
            model.addAttribute(ValidationConstants.MODEL_NAME, new DbCredentials());
        }

        return view("conn/db-connection", "providers", DatabaseProvider.values());
    }

    @PostMapping("/connect")
    public ModelAndView databaseConnectionPostAction(@Valid @ModelAttribute DbCredentialsBindingModel bindingModel,
                                                     BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(ValidationConstants.MODEL_NAME, bindingModel);
        redirectAttributes.addFlashAttribute(ValidationConstants.BINDING_RESULT_NAME, bindingResult);

        if (bindingResult.hasErrors()) {
            return redirect("connect");
        }

        final ConnectionManager connectionManager = bindingModel.getDatabaseProvider().getConnectionManager();
        DbCredentials dbCredentials = this.modelMapper.map(bindingModel, DbCredentials.class);

        if (!connectionManager.testJdbcConnection(dbCredentials)) {
            bindingResult.addError(new FieldError("model", "host", "Could not connect to host with provided parameters"));
            return redirect("connect");
        }

        UserDbConnection userDbConnection = connectionManager.connectWithORM(dbCredentials, this.entityMappingService.getMappedEntities());

        this.sessionDbConnection.setConnection(userDbConnection);
        this.connectionStorageService.addConnection(userDbConnection);

        return redirect("/");
    }

    @GetMapping("/destroy")
    public ModelAndView destroyConnectionAction() {
        if (this.sessionDbConnection.hasOpenConnection()) {
            this.sessionDbConnection.getConnection().closeConnections();
        }

        return redirect("/");
    }
}
