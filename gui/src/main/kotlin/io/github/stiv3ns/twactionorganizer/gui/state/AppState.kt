package io.github.stiv3ns.twactionorganizer.gui.state

import androidx.compose.runtime.*
import io.github.stiv3ns.twactionorganizer.core.*
import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerType
import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class Category {
    data object WorldConfig : Category()
    data object ConcreteResources : Category()
    data object FakeResources : Category()
    data object DemolitionResources : Category()
    data class TargetGroupItem(val group: TargetGroup) : Category()
    data object Assignments : Category()
    data object MapView : Category()
}

@Stable
class AppState(private val coroutineScope: CoroutineScope) {
    val uow = UnitOfWork()
    val logStore = LogStore()

    var world: World? by mutableStateOf(null)
        private set

    var concreteResources: Resources by mutableStateOf(Resources.empty())
        private set
    var fakeResources: Resources by mutableStateOf(Resources.empty())
        private set
    var demolitionResources: Resources by mutableStateOf(Resources.empty())
        private set

    var targetGroups = mutableStateListOf<TargetGroup>()
        private set

    var selectedCategory: Category by mutableStateOf(Category.WorldConfig)

    var assignments: List<Assignment> by mutableStateOf(emptyList())
        private set

    var isExecuting: Boolean by mutableStateOf(false)
        private set

    init {
        coroutineScope.launch {
            logStore.startSubscriptions()
        }
    }

    fun updateWorld(newWorld: World) {
        world = newWorld
        uow.setWorld(newWorld)
        logStore.addInfo("World set: ${newWorld.domain}")
    }

    fun updateConcreteResources(resources: Resources) {
        concreteResources = resources
        uow.setConcreteResources(resources)
        logStore.addInfo("Concrete resources set: ${resources.villageCount} villages, ${resources.playerCount} players")
    }

    fun updateFakeResources(resources: Resources) {
        fakeResources = resources
        uow.setFakeResources(resources)
        logStore.addInfo("Fake resources set: ${resources.villageCount} villages, ${resources.playerCount} players")
    }

    fun updateDemolitionResources(resources: Resources) {
        demolitionResources = resources
        uow.setDemolitionResources(resources)
        logStore.addInfo("Demolition resources set: ${resources.villageCount} villages, ${resources.playerCount} players")
    }

    fun addTargetGroup(group: TargetGroup) {
        uow.addTargetGroup(group)
        targetGroups.add(group)
        logStore.addInfo("Target group added: ${group.name} (${group.villageCount} villages, ${group.totalAttackCount} attacks)")
    }

    fun removeTargetGroup(group: TargetGroup) {
        uow.dropTargetGroup(group)
        targetGroups.remove(group)
        if (selectedCategory is Category.TargetGroupItem &&
            (selectedCategory as Category.TargetGroupItem).group == group
        ) {
            selectedCategory = Category.WorldConfig
        }
        logStore.addInfo("Target group removed: ${group.name}")
    }

    fun clearConcreteResources() {
        concreteResources = Resources.empty()
        uow.setConcreteResources(Resources.empty())
        logStore.addInfo("Concrete resources cleared")
    }

    fun clearFakeResources() {
        fakeResources = Resources.empty()
        uow.setFakeResources(Resources.empty())
        logStore.addInfo("Fake resources cleared")
    }

    fun clearDemolitionResources() {
        demolitionResources = Resources.empty()
        uow.setDemolitionResources(Resources.empty())
        logStore.addInfo("Demolition resources cleared")
    }

    @OptIn(kotlinx.coroutines.ObsoleteCoroutinesApi::class)
    fun execute() {
        if (isExecuting) return
        isExecuting = true
        coroutineScope.launch(Dispatchers.Default) {
            try {
                val executor = Executor(uow)
                val result = executor.executeAsync().await()
                assignments = result
                selectedCategory = Category.Assignments
                logStore.addInfo("Execution complete: ${result.size} assignments generated")
            } catch (e: Exception) {
                logStore.addError("Execution failed: ${e.message}")
            } finally {
                isExecuting = false
            }
        }
    }
}
