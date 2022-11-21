package ${package}.service;

import org.springframework.data.domain.Page;
import ${package}.dto.${pascalName}DTO;
import ${package}.dto.${pascalName}QueryPageDTO;
import ${package}.vo.${pascalName}VO;
import java.util.List;

public interface ${pascalName}Service {

// 获取${tableComment}分页列表
Page<${pascalName}VO> queryPage(${pascalName}QueryPageDTO dto);

  // 获取${tableComment}列表
  List<${pascalName}VO> queryList(${pascalName}DTO dto);

  // 获取${tableComment}详情
  ${pascalName}VO get(Long id);

  // 新增${tableComment}
  ${pascalName}VO add(${pascalName}DTO dto);

  // 编辑${tableComment}
  ${pascalName}VO edit(${pascalName}DTO dto);

  // 删除${tableComment}
  void delete(Long id);
}
