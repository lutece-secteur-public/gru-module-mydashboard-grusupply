/*
 * Copyright (c) 2002-2025, Mairie de Paris
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

package fr.paris.lutece.plugins.mydashboard.modules.grusupply.business;

import fr.paris.lutece.portal.service.spring.SpringContextService;
import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for TagDemandType objects
 */

public final class TagDemandTypeHome
{

    // Static variable pointed at the DAO instance

    private static ITagDemandTypeDAO _dao = ( ITagDemandTypeDAO ) SpringContextService.getBean( "mydashboard-grusupply.tagDemandTypeDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */

    private TagDemandTypeHome( )
    {
    }

    /**
     * Create an instance of the tagDemandType class
     * 
     * @param tagDemandType
     *            The instance of the TagDemandType which contains the informations to store
     * @return The instance of tagDemandType which has been created with its primary key.
     */

    public static TagDemandType create( TagDemandType tagDemandType )
    {
        _dao.insert( tagDemandType);

        return tagDemandType;
    }

    /**
     * Update of the tagDemandType which is specified in parameter
     * 
     * @param tagDemandType
     *            The instance of the TagDemandType which contains the data to store
     * @return The instance of the tagDemandType which has been updated
     */

    public static TagDemandType update( TagDemandType tagDemandType )
    {
        _dao.store( tagDemandType);

        return tagDemandType;
    }

    /**
     * Remove the tagDemandType whose identifier is specified in parameter
     * 
     * @param nTagDemandTypeId
     *            The tagDemandType Id
     */

    public static void remove( int nTagDemandTypeId )
    {
        _dao.delete( nTagDemandTypeId);
    }
    
    /**
     * Remove the tagDemandType by id demand type
     * 
     * @param nIdDemandType
     *            The demandType id
     */

    public static void removeByIdDemandType( int nIdDemandType )
    {
        _dao.deleteByIdDemandType( nIdDemandType);
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

    public static TagDemandType findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey );
    }
    
    /**
     * Load the data of all the tagDemandType objects and returns them in form of a list
     * 
     * @param nIdDemandType
     *            The id demand type
     * @return the list which contains the data of all the tagDemandType objects
     */

    public static List<TagDemandType> getTagDemandTypesList( int nIdDemandType )
    {
        return _dao.selectTagDemandTypesList( nIdDemandType );
    }

    /**
     * Load the data of all the tagDemandType objects and returns them in form of a list
     * 
     * @return the list which contains the data of all the tagDemandType objects
     */

    public static List<TagDemandType> getTagDemandTypesList( )
    {
        return _dao.selectTagDemandTypesList( );
    }

}
