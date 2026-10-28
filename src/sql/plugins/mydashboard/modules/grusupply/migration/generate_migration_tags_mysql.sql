--
-- LUT-32861 - Migration of the demand type tags to the notificationstore_demand_type metadata
--
-- To be executed on the database containing mydashboard_grusupply_tag_demand_type (MySQL / MariaDB).
-- Generates one UPDATE statement per tag, to be executed on the notificationstore database.
-- See README.md
--
SELECT CONCAT(
    'UPDATE notificationstore_demand_type SET meta_data = JSON_SET(COALESCE(NULLIF(TRIM(meta_data), \'\'), \'{}\'), ',
    QUOTE( CONCAT( '$."TAG_', REPLACE( tag, '"', '\\"' ), '"' ) ),
    ', \'true\') WHERE demande_type_id = ',
    QUOTE( CAST( id_demand_type AS CHAR ) ),
    ';'
)
FROM mydashboard_grusupply_tag_demand_type
ORDER BY id_demand_type, tag;
