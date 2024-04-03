package com.example.myHealth;

import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;

public class FoodsAdded implements Serializable {

    String food;
    String quantity;
    String phosphorus, card_prot_amt, card_pot_amt;
    String card_phos_header, card_prot_header, card_pot_header;

    DocumentReference documentPath;

    public FoodsAdded(){

    }

    public FoodsAdded(String fName, String fQuantity, String card_phos_amt, String card_phos_header)
    {
        this.food = fName;
        this.quantity = fQuantity;
        this.phosphorus = card_phos_amt;
        this.card_phos_header = card_phos_header;
    }

    public String getFoodName()
    {
        return food;
    }

    public void setFoodName(String fName)
    {
        this.food = fName;
    }

    public String getQuantity()
    {
        return quantity;
    }

    public void setQuantity(String fQuantity)
    {
        this.quantity = fQuantity;
    }

    public String getPhosphorus() {return phosphorus;}
    public void setPhosphorus(String cPhosAmt) {this.phosphorus = cPhosAmt;}

    public String getCard_phos_header() {return card_phos_header;}
    public void setCard_phos_header(String cPhosHeader) {this.card_phos_header = cPhosHeader;}

    public DocumentReference getDocumentPath() { return documentPath; }
    public void setDocumentPath(DocumentReference documentPath) { this.documentPath = documentPath; }

}
