package ${package}.repository;

import ${package}.model.${pascalName};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ${pascalName}Repository extends JpaRepository<${pascalName}, Long>,
    JpaSpecificationExecutor<${pascalName}> {

}
