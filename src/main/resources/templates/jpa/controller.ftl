package ${package}.controller;

import org.springframework.data.domain.Page;
import ${package}.dto.${pascalName}DTO;
import ${package}.dto.${pascalName}QueryPageDTO;
import ${package}.service.${pascalName}Service;
import ${package}.vo.${pascalName}VO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "${tableComment}")
@RequestMapping("/${camelName}s")
@RequiredArgsConstructor
public class ${pascalName}Controller {

  private final ${pascalName}Service ${camelName}Service;

  @GetMapping(value = "/queryPage")
  @Operation(description = "获取${tableComment}分页列表")
  public Result<PageResult<${pascalName}VO>> queryPage(@RequestBody ${pascalName}QueryPageDTO dto) {
    Page<${pascalName}VO> ${camelName}VOPage = ${camelName}Service.queryPage(dto);
    return Result.ok(PageResult.ok(${camelName}VOPage));
  }

  @GetMapping
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

  @PostMapping
  @Operation(description = "新增${tableComment}")
  public Result<${pascalName}VO> add(@RequestBody ${pascalName}DTO dto) {
    ${pascalName}VO ${camelName}VO = ${camelName}Service.add(dto);
    return Result.ok(${camelName}VO);
  }

  @PutMapping
  @Operation(description = "编辑${tableComment}")
  public Result<${pascalName}VO> edit(@RequestBody ${pascalName}DTO dto) {
    ${pascalName}VO ${camelName}VO = ${camelName}Service.edit(dto);
    return Result.ok(${camelName}VO);
  }

  @DeleteMapping
  @Operation(description = "删除${tableComment}")
  public Result<Object> delete(@RequestParam Long id) {
   ${camelName}Service.delete(id);
    return Result.ok();
  }
}