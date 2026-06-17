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
package fr.paris.lutece.plugins.mydashboard.modules.grusupply.web;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.grubusiness.business.notification.EnumNotificationType;
import fr.paris.lutece.plugins.grubusiness.business.web.rs.DemandResult;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.service.NotificationGruService;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.service.TagDemandTypeService;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.util.GrusupplyConstants;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.util.MydashboardGrusupplyUtil;
import fr.paris.lutece.plugins.mydashboard.service.MyDashboardComponent;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * 
 * MyDashboardComponentCompletedBilling
 *
 */
public class MyDashboardComponentCompletedBilling extends MyDashboardComponent
{

    /**
     * 
     */
    private static final long      serialVersionUID                   = 8297192924908575568L;

    // CONSTANTS
    private static final String    TEMPLATE_NOTIFICATION_LIST         = "skin/plugins/mydashboard/modules/grusupply/dashboard_completed_billing.html";
    private static final String    DASHBOARD_COMPONENT_ID             = "mydashboard-grusupply.componentCompletedBilling";
    private static final String    MESSAGE_COMPONENT_DESCRIPTION      = "module.mydashboard.grusupply.myDashboardComponentCompletedBilling.description";
    private static final String    CURRENT_PAGE_INDEX                 = "current_page_index_cb";   
    private static final String    PARAMETER_INDEX_PAGE_BILLING       = "page_index_cb";

    @Override
    public String getDashboardData( HttpServletRequest request )
    {
        final LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
        final String strCategoryCode = request.getParameter( GrusupplyConstants.PARAMETER_CATEGORY_CODE );
        
        if ( user == null )
        {
            return StringUtils.EMPTY;
        }
        
        Map<String, Object> model = new HashMap<>( );            
        final String strCustomerId = MydashboardGrusupplyUtil.getCustomerId(user.getName(),request);
        DemandResult demandResult = null;
        
        String strDemandTypeIds = TagDemandTypeService.getInstance( ).getListDemandTypeByTag( 
                GrusupplyConstants.PROPERTY_TAG_FACTURE, strCategoryCode )
                .stream( )
                .map( demandType -> String.valueOf( demandType.getIdDemandType( ) ) )
                .collect( Collectors.joining(",") );
        
        if(StringUtils.isNotEmpty(strDemandTypeIds)) {
	        demandResult = NotificationGruService.getInstance( ).getListDemandByStatus( 
	                strCustomerId, 
	                MydashboardGrusupplyUtil.getListStatus( GrusupplyConstants.PROPERTY_TAG_FACTURE, true, false ),
	                strDemandTypeIds,
	                null, 
	                null, 
	                EnumNotificationType.MYDASHBOARD.toString( ), 
	                null
	        );
        }
        
        MydashboardGrusupplyUtil.getDashboardData(
                request, 
                demandResult, 
                strCustomerId, 
                strCategoryCode,
                CURRENT_PAGE_INDEX,
                PARAMETER_INDEX_PAGE_BILLING, 
                model 
        );

        HtmlTemplate htmTemplate = AppTemplateService.getTemplate( TEMPLATE_NOTIFICATION_LIST, request.getLocale( ), model );
        return htmTemplate.getHtml( );
    }

    @Override
    public String getComponentId( )
    {
        return DASHBOARD_COMPONENT_ID;
    }

    @Override
    public String getComponentDescription( Locale locale )
    {
        return I18nService.getLocalizedString( MESSAGE_COMPONENT_DESCRIPTION, locale );
    }
}