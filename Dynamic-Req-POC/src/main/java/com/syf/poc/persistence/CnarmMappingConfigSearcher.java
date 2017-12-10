package com.syf.poc.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.syf.poc.persistence.model.CnarmMappingConfigVo;

@Repository
public class CnarmMappingConfigSearcher {

	@PersistenceContext
	private EntityManager em;
	
	public List<CnarmMappingConfigVo> getAllConfigVo(String groupId) {
		TypedQuery<CnarmMappingConfigVo> query = em.createQuery("select c from CnarmMappingConfigVo c where c.groupName = :groupNm", CnarmMappingConfigVo.class);
		query.setParameter("groupNm", groupId);
		List<CnarmMappingConfigVo> list = query.getResultList();
		return list;
	}
	
}
