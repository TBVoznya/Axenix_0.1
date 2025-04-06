from sqlalchemy import Column, Integer, String, Float, ForeignKey
from sqlalchemy.orm import relationship
from sqlalchemy.ext.declarative import declarative_base

# Создание базового класса для моделей
Base = declarative_base()

# Модель таблицы product_types
class ProductType(Base):
    __tablename__ = "product_types"
    type_id = Column(Integer, primary_key=True, autoincrement=True)
    type_name = Column(String, unique=True, nullable=False)
    
    products = relationship("Product", back_populates="product_type")
# Модель таблицы products
class Product(Base):
    __tablename__ = "products"
    id = Column(Integer, primary_key=True, autoincrement=True)
    name_product = Column(String, nullable=False)
    cost_product = Column(String)  # SQLAlchemy не имеет прямого типа MONEY, используем String или Numeric
    type_id = Column(Integer, ForeignKey('product_types.type_id'), nullable=False)
    
    product_type = relationship("ProductType", back_populates="products")
    obj_contents = relationship("ObjContent", back_populates="product")

# Модель таблицы obj_location
class ObjLocation(Base):
    __tablename__ = "obj_location"
    obj_id = Column(Integer, primary_key=True, autoincrement=True)
    x_cord = Column(Float, nullable=False)
    y_cord = Column(Float, nullable=False)
    
    contents = relationship("ObjContent", back_populates="location")
    lens = relationship("Len", back_populates="location")

# Модель таблицы obj_content
class ObjContent(Base):
    __tablename__ = "obj_content"
    id_content = Column(Integer, primary_key=True, autoincrement=True)
    name_obj = Column(Integer, ForeignKey('obj_location.obj_id'), nullable=False)
    on_shelf = Column(Integer, ForeignKey('products.id'), default=None)
    count_on = Column(Integer, default=0)
    
    location = relationship("ObjLocation", back_populates="contents")
    product = relationship("Product", back_populates="obj_contents")

# Модель таблицы lens
class Len(Base):
    __tablename__ = "lens"
    id_content = Column(Integer, primary_key=True, autoincrement=True)
    name_obj = Column(Integer, ForeignKey('obj_location.obj_id'), nullable=False)
    x_fin = Column(Float, default=None)
    y_fin = Column(Float, default=None)
    
    location = relationship("ObjLocation", back_populates="lens")
    