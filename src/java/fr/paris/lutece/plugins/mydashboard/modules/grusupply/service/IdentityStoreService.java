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
package fr.paris.lutece.plugins.mydashboard.modules.grusupply.service;



import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.AuthorType;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.IdentityDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.RequestAuthor;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.ResponseStatusType;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.service.IdentityServiceExtended;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * 
 * IdentityStoreService
 *
 */
public class IdentityStoreService
{
    // BEAN
    public static final String  BEAN_IDENTITY_STORE_NAME            = "myluteceuser-gu.identitystore.service";
    public static final String  PROPERTY_IDENTITY_CLIENT_CODE       = AppPropertiesService.getProperty( "myluteceusergu.identitystore.client.code", "MyDashboard" );

    private static IdentityServiceExtended _identityService = SpringContextService.getBean( BEAN_IDENTITY_STORE_NAME ); 
    
    /**
     * Constructor
     */
    private IdentityStoreService( )
    {

    }

    /**
     * Get identity by guid
     * @param strGuid
     * @return the identity find
     */
    public static IdentityDto getIdentityByGuid( String strGuid)
    {
        IdentityDto identity = null;

        RequestAuthor requestAuthor = new RequestAuthor( );
        requestAuthor.setName( PROPERTY_IDENTITY_CLIENT_CODE );
        requestAuthor.setType( AuthorType.owner );
        
        try
        {
            IdentitySearchResponse response = _identityService.getIdentityByConnectionId( strGuid, PROPERTY_IDENTITY_CLIENT_CODE, requestAuthor );

            if ( response != null && !ResponseStatusType.NOT_FOUND.equals( response.getStatus( ).getType( ) ) 
                    && response.getIdentities( ) != null && !response.getIdentities( ).isEmpty( ) )
            {
                identity = response.getIdentities( ).get( 0 );
            }
        } catch ( IdentityStoreException | AppException ine )
        {
            AppLogService.info( "Error getting Identity with guid {} ", strGuid, ine );
        }
        return identity;

    }
}