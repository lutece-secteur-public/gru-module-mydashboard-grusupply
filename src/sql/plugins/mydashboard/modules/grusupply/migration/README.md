# Reprise de données LUT-32861 : tags des types de demande

À partir de la version 1.0.6, les tags des types de demande ne sont plus stockés dans la table
`mydashboard_grusupply_tag_demand_type`. Ils sont désormais stockés dans le champ `meta_data` (map JSON)
de la table `notificationstore_demand_type`, sous la forme :

```json
{ "TAG_<tag>": "true" }
```

Les deux tables étant sur des bases de données différentes, la reprise se fait en deux temps :
un script exécuté sur la base **source** génère le script d'`UPDATE` à exécuter sur la base **cible**.

> ⚠️ Ces scripts ne doivent pas être placés dans `upgrade/` : ils seraient joués sur la mauvaise base.

## Ordre des opérations

Le script `upgrade/update_db_mydashboard-grusupply-1.0.5-1.0.6.sql` supprime la table
`mydashboard_grusupply_tag_demand_type`. La génération doit donc être faite **avant** le déploiement de la 1.0.6.

1. Générer le script de migration sur la base source (mydashboard-grusupply).
2. Déployer la version 1.0.6 (suppression de l'ancienne table).
3. Exécuter le script généré sur la base cible (notificationstore).

## Vérification préalable (base cible)

`JSON_SET` échoue si un `meta_data` existant n'est pas un JSON valide. Pour lister les lignes concernées :

```sql
-- MySQL / MariaDB
SELECT id, meta_data FROM notificationstore_demand_type WHERE meta_data <> '' AND JSON_VALID( meta_data ) = 0;
```

## MySQL / MariaDB

```bash
# 1. Génération, sur la base source
#    -N : pas d'en-tête, -B : sortie brute, -r : pas d'échappement des caractères spéciaux
mysql -N -B -r -h <host_source> -u <user> -p <base_source> < generate_migration_tags_mysql.sql > migrate_tags.sql

# 2. Relecture de migrate_tags.sql, puis exécution sur la base cible
mysql -h <host_cible> -u <user> -p <base_cible> < migrate_tags.sql
```

Exemple de requête générée :

```sql
UPDATE notificationstore_demand_type SET meta_data = JSON_SET(COALESCE(NULLIF(TRIM(meta_data), ''), '{}'), '$."TAG_rdv"', 'true') WHERE demande_type_id = '42';
```

## PostgreSQL

```bash
# 1. Génération, sur la base source
psql -At -h <host_source> -U <user> -d <base_source> -f generate_migration_tags_postgresql.sql > migrate_tags.sql

# 2. Relecture de migrate_tags.sql, puis exécution sur la base cible
psql -h <host_cible> -U <user> -d <base_cible> -f migrate_tags.sql
```

## Comportement

- Les entrées déjà présentes dans `meta_data` sont conservées ; la clé `TAG_<tag>` est ajoutée ou écrasée.
- Un `meta_data` `NULL` ou vide est initialisé à `{}`.
- Un type de demande ayant plusieurs tags reçoit un `UPDATE` par tag ; les entrées se cumulent.
- Le script généré est rejouable sans effet de bord.
- Les valeurs sont échappées (`QUOTE()` / `format( %L )`), une apostrophe dans un tag ne casse pas le SQL généré.
