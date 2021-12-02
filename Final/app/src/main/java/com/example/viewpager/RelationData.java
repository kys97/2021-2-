package com.example.viewpager;

public class RelationData {

    String Relation_ID;
    String Patient_PK;
    String Protector_PK;
    String Patient_Name;
    String Protector_Name;

    public RelationData(String relation_id, String patient_id, String patient_name, String protector_id, String protector_name){
        this.Relation_ID = relation_id;
        this.Patient_PK = patient_id;
        this.Patient_Name = patient_name;
        this.Protector_PK = protector_id;
        this.Protector_Name = protector_name;
    }

    public String getRelation_ID(){ return Relation_ID; }
    public String getPatient_PK(){ return Patient_PK; }
    public String getPatient_Name(){ return Patient_Name; }
    public String getProtector_PK(){ return Protector_PK; }
    public String getProtector_Name(){ return Protector_Name; }

    public void setRelation_ID(String id){ this.Relation_ID = id; }
    public void setPatient_PK(String id){ this.Patient_PK = id; }
    public void setPatient_Name(String name){ this.Patient_Name = name; }
    public void setProtector_PK(String id){ this.Protector_PK = id; }
    public void setProtector_Name(String name){ this.Protector_Name = name; }

}
