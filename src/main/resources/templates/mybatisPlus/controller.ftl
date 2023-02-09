package ${package}.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msdn.mall.common.entity.PageResult;
import com.msdn.mall.common.entity.Result;
import ${package}.dto.${pascalName}DTO;
import ${package}.dto.${pascalName}QueryPageDTO;
import ${package}.service.${pascalName}Service;
import ${package}.vo.${pascalName}VO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "${tableComment}")
@RequestMapping("/${camelName}s")
@RequiredArgsConstructor
public class ${pascalName}Controller {

    private final ${pascalName}Service ${camelName}Service;

    @GetMapping("/queryPage")
    @Operation(description = "获取${tableComment}分页列表")
    public Result<PageResult<${pascalName}VO>> queryPage(@RequestBody ${pascalName}QueryPageDTO dto) {
      IPage<${pascalName}VO> ${camelName}VOPage = ${camelName}Service.queryPage(dto);
      return Result.ok(PageResult.ok(${camelName}VOPage));
    }

    @GetMapping("/list")
    @Operation(description = "获取${tableComment}列表")
    public Result<List<${pascalName}VO>> queryList(@RequestBody ${pascalName}DTO dto) {
      List<${pascalName}VO> ${camelName}VOList = ${camelName}Service.queryList(dto);
      return Result.ok(${camelName}VOList);
    }

    @GetMapping("/{id}")
    @Operation(description = "获取${tableComment}详情")
    public Result<${pascalName}VO> get(@PathVariable("id") Long id) {
      ${pascalName}VO ${camelName}VO = ${camelName}Service.get(id);
      return Result.ok(${camelName}VO);
    }

    @PostMapping("/add")
    @Operation(description = "新增${tableComment}")
    public Result<Object> add(@RequestBody ${pascalName}DTO dto) {
      boolean flag = ${camelName}Service.add(dto);
      if (!flag) {
        return Result.failed();
      }
      return Result.ok();
    }

    @PutMapping("/edit")
    @Operation(description = "编辑${tableComment}")
    public Result<Object> edit(@RequestBody ${pascalName}DTO dto) {
      boolean flag = ${camelName}Service.edit(dto);
      if (!flag) {
        return Result.failed();
      }
      return Result.ok();
    }

    @DeleteMapping("/delete")
    @Operation(description = "删除${tableComment}")
    public Result<Object> delete(@RequestParam String id) {
      boolean flag = ${camelName}Service.delete(id);
      if (!flag) {
        return Result.failed();
      }
      return Result.ok();
    }
}
