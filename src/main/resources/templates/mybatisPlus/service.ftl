package ${package}.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import ${package}.dto.${pascalName}DTO;
import ${package}.dto.${pascalName}QueryPageDTO;
import ${package}.vo.${pascalName}VO;
import java.util.List;

public interface ${pascalName}Service {

  // 获取${tableComment}分页列表
  IPage<${pascalName}VO> queryPage(${pascalName}QueryPageDTO dto);

  // 获取${tableComment}列表
  List<${pascalName}VO> queryList(${pascalName}DTO dto);

  // 获取${tableComment}详情
  ${pascalName}VO get(Long id);

  // 新增${tableComment}
  Boolean add(${pascalName}DTO dto);

  // 编辑${tableComment}
  Boolean edit(${pascalName}DTO dto);

  // 删除${tableComment}
  Boolean delete(String id);
}