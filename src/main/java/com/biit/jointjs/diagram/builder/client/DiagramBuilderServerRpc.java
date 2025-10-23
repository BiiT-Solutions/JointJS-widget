package com.biit.jointjs.diagram.builder.client;

/*-
 * #%L
 * JointJs Diagram Builder for Vaadin
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.vaadin.shared.communication.ServerRpc;

public interface DiagramBuilderServerRpc extends ServerRpc {

    void toJsonAnswer(String jsonString);

    void pickedNode(String jsonString);

    void pickedConnection(String jsonString);

    void getFocus();

    void doubleClickNode(String jsonString);

    void addElement(String jsonString);

    void updateElement(String jsonString);

    void removeElement(String jsonString);
}
