/*
 * Copyright (c) 2002-2023, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.mydashboard.modules.grusupply.util;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import fr.paris.lutece.plugins.grubusiness.business.demand.Demand;
import fr.paris.lutece.plugins.grubusiness.business.demand.DemandCategory;
import fr.paris.lutece.plugins.grubusiness.business.demand.DemandType;
import fr.paris.lutece.plugins.grubusiness.business.notification.EnumNotificationType;
import fr.paris.lutece.plugins.grubusiness.business.notification.Notification;
import fr.paris.lutece.plugins.grubusiness.business.web.rs.DemandDisplay;
import fr.paris.lutece.plugins.grubusiness.business.web.rs.DemandResult;
import fr.paris.lutece.plugins.grubusiness.business.web.rs.EnumGenericStatus;
import fr.paris.lutece.plugins.grubusiness.business.web.rs.NotificationResult;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.IdentityDto;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.business.DemandDashboard;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.business.DemandDashboardHome;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.service.IdentityStoreService;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.service.NotificationGruService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.html.AbstractPaginator;

/**
 * 
 * MydashboardGrusupplyUtil
 *
 */
public class MydashboardGrusupplyUtil
{
    /**
     * Constructor
     */
    private MydashboardGrusupplyUtil( )
    {

    }

    /**
     * Get init ObjectMpper
     * 
     * @return objectMapper
     */
    public static ObjectMapper getObjectMapper( )
    {
        ObjectMapper mapper = new ObjectMapper( );
        mapper.configure( DeserializationFeature.UNWRAP_ROOT_VALUE, false );
        mapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
        mapper.configure( SerializationFeature.WRAP_ROOT_VALUE, false );
        mapper.configure( Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true );

        return mapper;
    }
    
    /**
     * Get list status
     * @param tag
     * @param includeFinalStatus
     * @param includeNonFinalStatus
     * @return list of status
     */
    public static String getListStatus(String tag, boolean includeFinalStatus, boolean includeNonFinalStatus) {
        Stream<EnumGenericStatus> stream;

        if (GrusupplyConstants.PROPERTY_TAG_FACTURE.equalsIgnoreCase(tag)) {
            String propertyValue = AppPropertiesService.getProperty(GrusupplyConstants.PROPERTY_LIST_STATUS_BILLING);
            if (propertyValue == null || propertyValue.isEmpty()) {
                return "";
            }

            stream = Arrays.stream(propertyValue.split(","))
                .map(String::trim)
                .map(name -> {
                    try {
                        return EnumGenericStatus.valueOf(name);
                    } catch (IllegalArgumentException e) {
                        AppLogService.debug("Unknown billing status: " + name);
                        return null;
                    }
                })
                .filter(Objects::nonNull);
        } else {
            stream = Arrays.stream(EnumGenericStatus.values());
        }

        return stream
            .filter(s -> (s.isFinalStatus() && includeFinalStatus) ||
                         (!s.isFinalStatus() && includeNonFinalStatus))
            .map(s -> String.valueOf(s.getStatusId()))
            .collect(Collectors.joining(","));
    }

    /**
     * Gets the list of {@link DemandDashboard} objects.
     * <p>
     * For each demand, retrieves related notifications and enriches the data using {@link DemandDashboardHome#selectByDemandIds(List)}.
     *
     * @param strCustomerId  the current customer id
     * @param demandResult the result containing the list of demands to display
     * @return list of demandDashboard
     */
    public static List<DemandDashboard> getDemandDashboards(String strCustomerId, DemandResult demandResult) {
        if (demandResult == null || CollectionUtils.isEmpty(demandResult.getListDemandDisplay())) {
            return Collections.emptyList();
        }

        List<DemandDisplay> displays = demandResult.getListDemandDisplay();

        // Récupération des IDs      
        List<Map<String, String>> listDemandPairs = displays.stream()
        	    .map(d -> {
        	        Map<String, String> map = new HashMap<>();
        	        map.put("demandId",d.getDemand().getId());
        	        map.put("demandTypeId",d.getDemand().getTypeId());
        	        return map;
        	    })
        	    .collect(Collectors.toList());

        // Récupération des notifications
        NotificationResult notifications = NotificationGruService.getInstance()
                .getListNotification(strCustomerId, listDemandPairs, EnumNotificationType.MYDASHBOARD.name());

        // Map notifications par ID de demande
        Map<String, List<Notification>> mapNotifications = (notifications != null)
                ? notifications.getNotifications().stream()
                    .collect(Collectors.groupingBy(
                            n -> n.getDemand().getId(),
                            Collectors.toList()))
                : Collections.emptyMap();

        // Construction des dashboards
        return displays.stream()
                .map(dd -> {
                    Demand demand = dd.getDemand();
                    DemandDashboard dashboard = new DemandDashboard(demand.getUID(), false);

                    dashboard.setDemand(demand);
                    dashboard.setStatus(dd.getStatus());
                    dashboard.setListNotification(mapNotifications.getOrDefault(demand.getId(), Collections.emptyList()));

                    return dashboard;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Set model
     * @param model
     * @param nDefaultItemsPerPage
     * @param paginator
     * @param listDemandType
     */
    public static void setModel( Map<String, Object> model, int nDefaultItemsPerPage, LocalizedPaginator<DemandDashboard> paginator, List<DemandType> listDemandType )
    {
        model.put( GrusupplyConstants.MARK_DEMAND_TYPE_LIST, listDemandType );
        model.put( GrusupplyConstants.MARK_NB_ITEMS_PER_PAGE, nDefaultItemsPerPage );
        model.put( GrusupplyConstants.MARK_PAGINATOR, paginator );
        model.put( GrusupplyConstants.MARK_LIST_DEMAND, paginator.getPageItems( ) );
    }
    
    
    /**
     * Returns the list of categories of user demands
     * @param strDashboardComponentId
     * @param strCustomerId
     * @param categoryCode
     * @param strDemandTypeIds
     * @param strListStatus
     * @param request
     * @return the list of categories of user demands
     */
    public static void addListCategoriesToModel(
            String strDashboardComponentId,
            String strCustomerId,
            String categoryCode,
            String strDemandTypeIds,
            String strListStatus,
            Map<String,Object> model,
            HttpServletRequest request
        ) {
        
            String strSessionAttribute = GrusupplyConstants.SESSION_CATEGORIES + strDashboardComponentId;
            @SuppressWarnings("unchecked")
            Map<String, String> categories = (Map<String, String>) request.getSession().getAttribute(strSessionAttribute);

            if (categories == null || categories.isEmpty()) {
                try {
                    //Demand list
                    DemandResult demandResult = NotificationGruService.getInstance()
                            .getListDemandByStatus(strCustomerId, strListStatus, strDemandTypeIds, null,
                                    "20000", EnumNotificationType.MYDASHBOARD.toString(), categoryCode);
                    
                    List<DemandType> listDemandTypes = NotificationGruService.getInstance( ).getListDemandType( );    
                    List<DemandCategory> allCategories = NotificationGruService.getInstance().getListDemandCategories();
    
                    //Map of categories
                    Map<String, String> categoryCodeToLabel = allCategories.stream()
                            .collect(Collectors.toMap(DemandCategory::getCode, DemandCategory::getLabel));
    
                    //Map of demandType
                    Map<Integer, DemandType> demandTypeById = listDemandTypes.stream()
                            .collect(Collectors.toMap(DemandType::getIdDemandType, Function.identity()));
    
                    categories = new HashMap<>();
    
                    //Retrieving demand categories
                    if(demandResult != null && demandResult.getListDemandDisplay() != null) {
                        for (DemandDisplay demandDisplay : demandResult.getListDemandDisplay()) {
                            Demand demand = demandDisplay.getDemand();
                            if (demand == null || StringUtils.isEmpty(demand.getTypeId())) {
                                continue;
                            }
        
                            int typeId = Integer.parseInt(demand.getTypeId());
                            DemandType demandType = demandTypeById.get(typeId);
                            if (demandType != null) {
                                String categoryCodeType = demandType.getCategory();
                                String categoryLabel = categoryCodeToLabel.get(categoryCodeType);
                                if (categoryLabel != null) {
                                    categories.put(categoryCodeType, categoryLabel);
                                }
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    AppLogService.error( "Une erreur s'est produite lors de la récupération des categories des demandes", e.getMessage( ));
                }
                //Cache session
                request.getSession().setAttribute(strSessionAttribute, categories);
            }

            model.put( GrusupplyConstants.MARK_LIST_CATEGORIES, categories);
        }
    
    
    /**
     * Gets customer id
     * @param strGuid
     * @param request
     * @return customer id
     */
    public static String getCustomerId(String strGuid, HttpServletRequest request){
        String strCustomerId = ( String ) request.getSession( ).getAttribute( GrusupplyConstants.SESSION_CUSTOMER_ID + strGuid);
        
        if(StringUtils.isEmpty( strCustomerId )){
            IdentityDto identity = IdentityStoreService.getIdentityByGuid(strGuid);
               
            if(identity!=null){                
                request.getSession( ).setAttribute(GrusupplyConstants.SESSION_CUSTOMER_ID + strGuid, identity.getCustomerId( ));
                return identity.getCustomerId( );
            }
        }        
        return strCustomerId;
    }
    
    /**
     * Get dashboard data
     * @param request
     * @param demandResult
     * @param strCustomerId
     * @param strCategoryCode
     * @param model
     */
    public static void getDashboardData(
            HttpServletRequest request, 
            DemandResult demandResult, 
            String strCustomerId, 
            String strCategoryCode,
            String strSessionCurrentPageIndex,
            String strParameterIndexPage,
            Map<String, Object> model 
    ) {

        final String strInputDate = request.getParameter(GrusupplyConstants.PARAMETER_INPUT_DATE);
        final String strInputSearch = request.getParameter(GrusupplyConstants.PARAMETER_INPUT_SEARCH);
        final String strPanel = request.getParameter(GrusupplyConstants.PARAMETER_PANEL);

        // URL mes demarches
        final StringBuilder urlBuilder = new StringBuilder(
                AppPropertiesService.getProperty(GrusupplyConstants.PROPERTY_URL_MES_DEMARCHES)
        ).append(strPanel);

        if (StringUtils.isNotEmpty(strCategoryCode)) {
            urlBuilder.append("&cat=").append(strCategoryCode);
        }
        
        final String strUrl = urlBuilder.toString();

        // Pagination
        final HttpSession session = request.getSession(true);
        String strCurrentPageIndex = (String) session.getAttribute(strSessionCurrentPageIndex);

        strCurrentPageIndex = AbstractPaginator.getPageIndex(
                request,
                strParameterIndexPage,
                strCurrentPageIndex
        );
        session.setAttribute(strSessionCurrentPageIndex, strCurrentPageIndex);

        final int nDefaultItemsPerPage = AppPropertiesService.getPropertyInt(GrusupplyConstants.PROPERTY_NUMBER_OF_DEMAND_PER_PAGE, 10);

        // Pas de résultat.
        if (demandResult == null || CollectionUtils.isEmpty(demandResult.getListDemandDisplay())) {
            return;
        }

        //Instance NotificationService
        final NotificationGruService notificationService = NotificationGruService.getInstance();

        List<DemandDashboard> listDemandDashboards = MydashboardGrusupplyUtil.getDemandDashboards(strCustomerId, demandResult);

        final List<DemandType> listDemandType = notificationService.getListDemandType();

        // Filtres
        if (StringUtils.isNotEmpty(strInputDate)) {
            listDemandDashboards = notificationService.filterByDate(
                    listDemandDashboards,
                    LocalDate.parse(strInputDate)
            );
        }

        if (StringUtils.isNotEmpty(strInputSearch)) {
            listDemandDashboards = notificationService.filterByKeyword(
                    listDemandDashboards,
                    strInputSearch.toLowerCase(),
                    listDemandType
            );
        }

        // Pagination finale
        final LocalizedPaginator<DemandDashboard> paginator =
                new LocalizedPaginator<>(
                        listDemandDashboards,
                        nDefaultItemsPerPage,
                        strUrl,
                        strParameterIndexPage,
                        strCurrentPageIndex,
                        request.getLocale()
                );

        MydashboardGrusupplyUtil.setModel(
                model,
                nDefaultItemsPerPage,
                paginator,
                listDemandType
        );
    }
}
