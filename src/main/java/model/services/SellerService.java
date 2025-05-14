package model.services;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

import java.util.List;

public class SellerService {

    private SellerDao sellerDao = DaoFactory.createSellerDao();

    public List<Seller> findAll() {
        return sellerDao.findAll();
    }

    public int insertSeller(Seller Seller) {
        return sellerDao.insert(Seller);
    }

    public void updateSeller(Seller Seller){
            sellerDao.update(Seller);
    }

    public Seller findById(Integer id){
        return sellerDao.findById(id);
    }

    public Seller findByName(String name){
        return sellerDao.findByName(name);
    }

    public void deleteSeller(Seller Seller){
        sellerDao.deleteById(Seller.getId());
    }
}
