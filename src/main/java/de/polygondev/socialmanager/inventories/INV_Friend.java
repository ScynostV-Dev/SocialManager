package de.polygondev.socialmanager.inventories;

import de.polygondev.inventoryapi.inventory.Inventory;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class INV_Friend extends Inventory {

    /**
     * Create your Inventory
     *
     * @param title The title of the Inventory (can have Colors)
     * @param name
     */
    public INV_Friend(String title, String name) {
        super(title, name);
        this.setRows(6);
    }

    @Override
    public void clickEvent(InventoryClickEvent e) {

    }

    @Override
    public void closeEvent(InventoryCloseEvent e) {

    }

}
