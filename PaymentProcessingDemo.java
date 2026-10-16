import java.util.*;

class Customer { private final String name; Customer(String name){this.name=name;} String getName(){return name;} }
class Product { private final String name; private final double price; Product(String name,double price){if(price<0)throw new IllegalArgumentException("Negative price");this.name=name;this.price=price;} String getName(){return name;} double getPrice(){return price;} }
class OrderItem { final Product product; final int quantity; OrderItem(Product p,int q){if(q<=0)throw new IllegalArgumentException("Quantity must be positive");product=p;quantity=q;} double total(){return product.getPrice()*quantity;} }
interface PaymentMethod { String getName(); boolean processPayment(double amount); }
class CreditCardPayment implements PaymentMethod { public String getName(){return "Credit Card";} public boolean processPayment(double amount){return true;} }
class PayPalPayment implements PaymentMethod { private final boolean succeeds; PayPalPayment(boolean succeeds){this.succeeds=succeeds;} public String getName(){return "PayPal";} public boolean processPayment(double amount){return succeeds;} }
class BankTransferPayment implements PaymentMethod { public String getName(){return "Bank Transfer";} public boolean processPayment(double amount){return true;} }
class Order {
    enum Status { PENDING, PAID }
    private final String orderId; private final Customer customer; private final List<OrderItem> items=new ArrayList<>(); private Status status=Status.PENDING;
    Order(String orderId,Customer customer){this.orderId=orderId;this.customer=customer;System.out.println("Order created for "+customer.getName()+".");}
    void addProduct(Product p,int quantity){if(status==Status.PAID)throw new IllegalStateException("Paid order cannot be modified");items.add(new OrderItem(p,quantity));}
    double total(){return items.stream().mapToDouble(OrderItem::total).sum();}
    Status getStatus(){return status;}
    void pay(PaymentMethod method){if(items.isEmpty()){System.out.println("Cannot process payment for an empty order.");return;}System.out.println("Payment initiated via "+method.getName()+" for Order "+orderId+".");if(method.processPayment(total())){status=Status.PAID;System.out.println("Payment for Order "+orderId+" successful.");}else{System.out.println("Payment for Order "+orderId+" failed.");}System.out.println("Order status: "+(status==Status.PAID?"Paid":"Pending"));}
}
public class PaymentProcessingDemo {
    public static void main(String[] args){Customer x=new Customer("Customer X"),y=new Customer("Customer Y"),z=new Customer("Customer Z");Order ox=new Order("X",x);ox.addProduct(new Product("Product A",20),2);ox.addProduct(new Product("Product B",15),1);ox.pay(new CreditCardPayment());Order oy=new Order("Y",y);oy.pay(new CreditCardPayment());Order oz=new Order("Z",z);oz.addProduct(new Product("Product C",50),1);oz.pay(new PayPalPayment(false));}
}
