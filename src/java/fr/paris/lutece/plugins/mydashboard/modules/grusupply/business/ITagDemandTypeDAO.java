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

import java.util.List;



 /**
 * ITagDemandTypeDAO Interface
 */

public interface ITagDemandTypeDAO
{


    /**
     * Insert a new record in the table.
     * @param tagDemandType instance of the TagDemandType object to inssert
     */

    void insert( TagDemandType tagDemandType);

     /**
     * Update the record in the table
     * @param tagDemandType the reference of the TagDemandType
     */

    void store( TagDemandType tagDemandType);


    /**
     * Delete a record from the table
     * @param nIdTagDemandType int identifier of the TagDemandType to delete
     */

    void delete( int nIdTagDemandType);
    
    /**
     * Delete a record from the table
     * @param nIdDemandType the demandType id
     */

    void deleteByIdDemandType( int nIdDemandType);

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Load the data from the table
     * @param strId The identifier of the tagDemandType
     * @return The instance of the tagDemandType
     */

    TagDemandType load( int nKey);


    /**
    * Load the data of all the tagDemandType objects and returns them as a List
    * @param nIdDemandType the id demand type
    * @return The List which contains the data of all the tagDemandType objects
    */

   List<TagDemandType> selectTagDemandTypesList( int nIdDemandType );

     /**
     * Load the data of all the tagDemandType objects and returns them as a List
     * @return The List which contains the data of all the tagDemandType objects
     */

    List<TagDemandType> selectTagDemandTypesList( );
    
}