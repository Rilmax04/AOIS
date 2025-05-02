package by.kurilo.lab6.hash;

public class Entry {
    private final String id;
    private int hasCollision;
    private int isUsed;
    private int isTail;
    private final int lockFlag;
    private int isDeleted;
    private Integer nextSlot;
    private String data;
    private final int keyValue;
    private final int hash;

    public Entry(String id, String data, int keyValue, int hash) {
        this.id = id;
        this.data = data;
        this.keyValue = keyValue;
        this.hash = hash;
        this.isUsed = 1;
        this.isTail = 1;
        this.lockFlag = 0;
        this.isDeleted = 0;
    }

    public String getId() {
        return id;
    }

    public int getHasCollision() {
        return hasCollision;
    }

    public int getIsUsed() {
        return isUsed;
    }

    public int getIsTail() {
        return isTail;
    }

    public int getLockFlag() {
        return lockFlag;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public Integer getNextSlot() {
        return nextSlot;
    }

    public String getData() {
        return data;
    }

    public int getKeyValue() {
        return keyValue;
    }

    public int getHash() {
        return hash;
    }

    public void setHasCollision(int hasCollision) {
        this.hasCollision = hasCollision;
    }

    public void setIsUsed(int isUsed) {
        this.isUsed = isUsed;
    }

    public void setIsTail(int isTail) {
        this.isTail = isTail;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setNextSlot(Integer nextSlot) {
        this.nextSlot = nextSlot;
    }

    public void setData(String data) {
        this.data = data;
    }

}
