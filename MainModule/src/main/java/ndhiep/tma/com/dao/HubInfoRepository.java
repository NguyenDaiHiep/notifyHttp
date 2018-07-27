package ndhiep.tma.com.dao;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ndhiep.tma.com.entity.HubInfo;


@Repository
public interface HubInfoRepository extends CrudRepository<HubInfo, Long> {
	@Query("SELECT h FROM HubInfo h")
	public ArrayList<HubInfo> findHubInfo();

}
