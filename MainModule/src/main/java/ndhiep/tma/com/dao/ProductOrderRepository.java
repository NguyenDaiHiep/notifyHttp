package ndhiep.tma.com.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ndhiep.tma.com.entity.ProductOrder;

@Repository
public interface ProductOrderRepository extends CrudRepository<ProductOrder, String>{

}
