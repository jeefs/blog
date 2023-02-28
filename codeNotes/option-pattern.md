###  使用场景

```go
//user.go 定义用户dao 
type DBOption func(db *gorm.DB) *gorm.DB

type UserDao struct {
	dao Dao
}

func (e UserDao) GetUser(paging *resp.PageInfo, opts ...DBOption) ([]model.User, int64, error) {
	var res []model.User
	var total int64
	db := e.dao.Engine.Table("user")
	for _, opt := range opts {
		db = opt(db)
	}
	db2 := db
	if err := db2.Count(&total).Error; err != nil {
		return nil, 0, err
	}
	db.Select("user.*")
	if paging != nil {
		db.Scopes(databases.Paginate(paging.Page, paging.PerPage))
	}
	if err := db.Find(&res).Error; err != nil {
		return nil, 0, err
	}
	return res, total, nil
}

func (e UserDao) WithById(id string) DBOption {
	return func(db *gorm.DB) *gorm.DB {
		return db.Where("a.id = ?", id)
	}
}

func (e UserDao) WithByAge(age int) DBOption {
	return func(db *gorm.DB) *gorm.DB {
		return db.Where("a.age = ?", age)
	}
}

func (e UserDao) WithBySex(sex int) DBOption {
	return func(db *gorm.DB) *gorm.DB {
		return db.Where("a.sex = ?", sex)
	}
}


//使用
newDao := dao.NewDao(context.Background(), databases.DB)
userDao := newDao.userDao()
res,err := userDao.GetUser(nil,userDao.WithByAge(25),userDao.WithBySex(1))

//选项模式合适用条件组合的场景，例如数据条件查询，能灵活组合不同查询条件，提高代码内聚
```

