package br.com.navis.transportadora.externo.infra.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

@Service
public class SchemaService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void createSchema(Long transportadoraId) { // TODO testar esse método
        try {
            var schemaName = "transportadora_" + transportadoraId;
            // Primeiro executa o comando para criar um novo schema
            entityManager.createNativeQuery("CREATE SCHEMA IF NOT EXISTS " + schemaName).executeUpdate();

            // Muda para o schema que forá gerado
            entityManager.createNativeQuery("SET SCHEMA " + schemaName).executeUpdate();

            // sincroniza as tabelas.
            createTableFromSqlFile(schemaName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTableFromSqlFile(String schemaName) {
        try {
            // Carrega o conteúdo do arquivo SQL
            Resource resource = new ClassPathResource("db/migration/create_tables.sql"); // ajustar o schema name.
            String sqlScript = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));

            // Substitui os marcadores de posição pelo nome do schema
            sqlScript = sqlScript.replace("{schemaName}", schemaName);

            // Executa o script SQL
            entityManager.createNativeQuery(sqlScript).executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
