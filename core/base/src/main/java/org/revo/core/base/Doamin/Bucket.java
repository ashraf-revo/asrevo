package org.revo.core.base.Doamin;

public class Bucket {
    private String name;
    private String domainName;
    private boolean accessible;

    public Bucket() {
    }

    public Bucket(String name, String domainName, boolean accessible) {
        this.name = name;
        this.domainName = domainName;
        this.accessible = accessible;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public void setAccessible(boolean accessible) {
        this.accessible = accessible;
    }

    @Override
    public String toString() {
        return name;
    }

}
