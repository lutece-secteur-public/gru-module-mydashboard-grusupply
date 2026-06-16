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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.grubusiness.business.demand.DemandType;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.business.TagDemandType;
import fr.paris.lutece.plugins.mydashboard.modules.grusupply.business.TagDemandTypeHome;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * 
 * TagDemandTypeService
 *
 */
public class TagDemandTypeService
{
    private static final String BEAN_NAME = "mydashboard-grusupply.tagDemandTypeService";
    
    private static TagDemandTypeService _tagDemandTypeService;
    
    /**
     * Private constructor
     */
    public TagDemandTypeService()
    {
        //Do nothing
    }
    
    /**
     * 
     * @return instance of TagDemandTypeService
     */
    public static TagDemandTypeService getInstance()
    {
        if(_tagDemandTypeService==null)
        {
            _tagDemandTypeService = (TagDemandTypeService) SpringContextService.getBean( BEAN_NAME );
        }
        
        return _tagDemandTypeService;
    }
    
    /**
     * Create an instance of the tagDemandType class
     * 
     * @param tagDemandType
     *            The instance of the TagDemandType which contains the informations to store
     * @return The instance of tagDemandType which has been created with its primary key.
     */

    public TagDemandType create( TagDemandType tagDemandType )
    {
        TagDemandTypeHome.create( tagDemandType);

        return tagDemandType;
    }

    /**
     * Update of the tagDemandType which is specified in parameter
     * 
     * @param tagDemandType
     *            The instance of the TagDemandType which contains the data to store
     * @return The instance of the tagDemandType which has been updated
     */

    public TagDemandType update( TagDemandType tagDemandType )
    {
        TagDemandTypeHome.update( tagDemandType);

        return tagDemandType;
    }

    /**
     * Remove the tagDemandType whose identifier is specified in parameter
     * 
     * @param nTagDemandTypeId
     *            The tagDemandType Id
     */

    public void remove( int nTagDemandTypeId )
    {
        TagDemandTypeHome.remove( nTagDemandTypeId);
    }
    
    /**
     * Remove the tagDemandType by id demand type
     * 
     * @param nIdDemandType
     *            The demandType id
     */

    public void removeByIdDemandType( int nIdDemandType )
    {
        TagDemandTypeHome.removeByIdDemandType( nIdDemandType);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a tagDemandType whose identifier is specified in parameter
     * 
     * @param nKey
     *            The tagDemandType primary key
     * @return an instance of TagDemandType
     */

    public TagDemandType findByPrimaryKey( int nKey )
    {
        return TagDemandTypeHome.findByPrimaryKey( nKey );
    }
    
    /**
     * Load the data of all the tagDemandType objects and returns them in form of a list
     * 
     * @param nIdDemandType
     *            The id demand type
     * @return the list which contains the data of all the tagDemandType objects
     */

    public List<TagDemandType> getTagDemandTypesList( int nIdDemandType )
    {
        return TagDemandTypeHome.getTagDemandTypesList( nIdDemandType );
    }

    /**
     * Load the data of all the tagDemandType objects and returns them in form of a list
     * 
     * @return the list which contains the data of all the tagDemandType objects
     */

    public List<TagDemandType> getTagDemandTypesList( )
    {
        return TagDemandTypeHome.getTagDemandTypesList( );
    }
    
    /**
     * Allows you to retrieve the list of type demands configured with the tag as a parameter
     * @param strTag
     * @param strCategory
     * @return list of demands type
     */
    public List<DemandType> getListDemandTypeByTag( String strTag, String strCategory )
    {       
        if(strTag == null || strTag.isEmpty( ) )
        {
            return Collections.emptyList( );
        }
        
        // Collect the IDs of demand types associated with the given tag
        Set<Integer> demandTypeIds = getTagDemandTypesList().stream()
                .filter(tag -> strTag.equals(tag.getTag( )))
                .map(TagDemandType::getIdDemandType)
                .collect(Collectors.toSet());
                
        
        // Filter and return only the demand types that match those IDs and category
        return NotificationGruService.getInstance( )
                .getListDemandType( ).stream( )
                .filter(demandType -> demandTypeIds.contains(demandType.getIdDemandType()))
                .filter(demandType -> StringUtils.isEmpty( strCategory ) || strCategory.equals( demandType.getCategory( ) )  )
                .collect(Collectors.toList());        
    }
    
    /**
     * Retrieve the list of untagged demand types
     * @param strCategory
     * @return list of demand types
     */
    public List<DemandType> getUntaggedDemandTypesList(String strCategory){     	
        // Collect the IDs of tagged demand types
        Set<Integer> taggedDemandTypeIds = getTagDemandTypesList().stream()
                .map(TagDemandType::getIdDemandType)
                .collect(Collectors.toSet());
    	
        // Filter and return only the untagged demand types
        return NotificationGruService.getInstance( )
                .getListDemandType( ).stream( )
                .filter(demandType -> !taggedDemandTypeIds.contains(demandType.getIdDemandType()))
                .filter(demandType -> StringUtils.isEmpty( strCategory ) || strCategory.equals( demandType.getCategory( ) )  )
                .collect(Collectors.toList());   
    }
}
