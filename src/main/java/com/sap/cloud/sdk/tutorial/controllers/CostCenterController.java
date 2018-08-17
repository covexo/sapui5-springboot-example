package com.sap.cloud.sdk.tutorial.controllers;

import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.ReadCreateUpdateAndDeleteOutboundDeliveriesNamespace;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.connectivity.ErpDestination;
import com.sap.cloud.sdk.s4hana.datamodel.bapi.structures.CostCenterCreateInput;
import com.sap.cloud.sdk.s4hana.datamodel.bapi.structures.ReturnParameter;
import com.sap.cloud.sdk.s4hana.datamodel.bapi.types.ControllingArea;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.ReadCostCenterDataNamespace.CostCenter;
import com.sap.cloud.sdk.s4hana.serialization.SapClient;
import com.sap.cloud.sdk.tutorial.command.CreateCostCenterCommand;
import com.sap.cloud.sdk.tutorial.command.GetCostCenterCommand;

@RestController
public class CostCenterController
{
    private static final Logger logger = CloudLoggerFactory.getLogger(CostCenterController.class);
    private static final List<CostCenter> costCenters = new ArrayList<CostCenter>();

    private ErpConfigContext getErpConfigContext( final String sapClient ){
        final ErpConfigContext config = new ErpConfigContext(
                ErpDestination.getDefaultName(),
                new SapClient(sapClient),
                Locale.ENGLISH);
        return config;
    }

    @RequestMapping( value = "api/v1/rest/client/{sapClient:[\\d]+}/costcenters", method = RequestMethod.GET )
    public ResponseEntity<List<CostCenter>> getCostCenter(
            @PathVariable final String sapClient )
    {
        try {
            return ResponseEntity.ok(CostCenterController.costCenters);
        }
        catch( final Exception e ) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping( value = "api/v1/rest/client/{sapClient:[\\d]+}/controllingarea/{controllingArea:[\\w]+}/costcenters", method = RequestMethod.POST )
    public ResponseEntity<List<ReturnParameter>> postCostCenter(
            @PathVariable final String sapClient,
            @PathVariable final ControllingArea controllingArea,
            @RequestBody final CostCenterCreateInput input,
            @RequestParam(defaultValue = "false") final boolean testRun )
    {
        try {
            CostCenter costCenter = new CostCenter();

            costCenter.setCostCenterDescription(input.getDescript());
            costCenter.setCostCenterID(input.getCostcenter().getValue());

            costCenters.add(costCenter);

            return ResponseEntity.ok(null);
        }
        catch( final Exception e ) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
