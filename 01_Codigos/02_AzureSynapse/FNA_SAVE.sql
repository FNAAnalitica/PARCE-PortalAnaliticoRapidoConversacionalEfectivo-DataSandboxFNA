COPY INTO dbo.Final
FROM 'https://dlsfnaparce.dfs.core.windows.net/test/storage_bot/*.parquet'
WITH (
    FILE_TYPE = 'PARQUET'
)