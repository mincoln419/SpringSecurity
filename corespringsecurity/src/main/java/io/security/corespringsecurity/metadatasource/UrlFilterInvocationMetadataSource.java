package io.security.corespringsecurity.metadatasource;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class UrlFilterInvocationMetadataSource implements FilterInvocationSecurityMetadataSource{

	private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap = new LinkedHashMap<>();

	public UrlFilterInvocationMetadataSource(LinkedHashMap<RequestMatcher, List<ConfigAttribute>> resources) {

		this.requestMap = resources;
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

		HttpServletRequest request = ((FilterInvocation)object).getRequest();

		requestMap.put(new AntPathRequestMatcher("/mypage"), Arrays.asList(new SecurityConfig("ROLE_USER")));

		if(requestMap != null) {
			for(Map.Entry<RequestMatcher, List<ConfigAttribute>> entry : requestMap.entrySet()) {
				RequestMatcher matcher = entry.getKey();
				if(matcher.matches(request)) {
					return entry.getValue();
				}
			}
		}
		return null;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		Set<ConfigAttribute> allatAttributes = new HashSet<>();

		for(Map.Entry<RequestMatcher, List<ConfigAttribute>> entry : requestMap.entrySet()) {
			allatAttributes.addAll(allatAttributes);
		}
		return allatAttributes;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

}
