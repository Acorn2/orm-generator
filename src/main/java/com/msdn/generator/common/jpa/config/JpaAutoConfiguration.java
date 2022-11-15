package com.msdn.generator.common.jpa.config;

import java.util.Optional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author hresh
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * @date 2022/9/5 4:13 下午
 * @description
 */
@Configuration
public class JpaAutoConfiguration implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    SecurityContext ctx = SecurityContextHolder.getContext();
    Object principal = ctx.getAuthentication().getPrincipal();
    if (principal.getClass().isAssignableFrom(String.class)) {
      return Optional.of((String) principal);
    } else {
      return null;
    }
  }
}
