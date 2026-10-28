--
-- LUT-32861 - Migration of the demand type tags to the notificationstore_demand_type metadata
--
-- To be executed on the database containing mydashboard_grusupply_tag_demand_type (PostgreSQL).
-- Generates one UPDATE statement per tag, to be executed on the notificationstore database.
-- See README.md
--
SELECT format(
    'UPDATE notificationstore_demand_type SET meta_data = (COALESCE(NULLIF(TRIM(meta_data), %L), %L)::jsonb || jsonb_build_object(%L, %L))::text WHERE demande_type_id = %L;',
    '', '{}', 'TAG_' || tag, 'true', id_demand_type::text )
FROM mydashboard_grusupply_tag_demand_type
ORDER BY id_demand_type, tag;
