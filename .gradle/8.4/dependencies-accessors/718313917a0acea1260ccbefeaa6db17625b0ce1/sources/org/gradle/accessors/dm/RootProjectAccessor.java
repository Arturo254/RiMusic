package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.internal.artifacts.dependencies.ProjectDependencyInternal;
import org.gradle.api.internal.artifacts.DefaultProjectDependencyFactory;
import org.gradle.api.internal.artifacts.dsl.dependencies.ProjectFinder;
import org.gradle.api.internal.catalog.DelegatingProjectDependency;
import org.gradle.api.internal.catalog.TypeSafeProjectDependencyFactory;
import javax.inject.Inject;

@NonNullApi
public class RootProjectAccessor extends TypeSafeProjectDependencyFactory {


    @Inject
    public RootProjectAccessor(DefaultProjectDependencyFactory factory, ProjectFinder finder) {
        super(factory, finder);
    }

    /**
     * Creates a project dependency on the project at path ":"
     */
    public RiMusicProjectDependency getRiMusic() { return new RiMusicProjectDependency(getFactory(), create(":")); }

    /**
     * Creates a project dependency on the project at path ":app"
     */
    public AppProjectDependency getApp() { return new AppProjectDependency(getFactory(), create(":app")); }

    /**
     * Creates a project dependency on the project at path ":compose-persist"
     */
    public ComposePersistProjectDependency getComposePersist() { return new ComposePersistProjectDependency(getFactory(), create(":compose-persist")); }

    /**
     * Creates a project dependency on the project at path ":compose-reordering"
     */
    public ComposeReorderingProjectDependency getComposeReordering() { return new ComposeReorderingProjectDependency(getFactory(), create(":compose-reordering")); }

    /**
     * Creates a project dependency on the project at path ":compose-routing"
     */
    public ComposeRoutingProjectDependency getComposeRouting() { return new ComposeRoutingProjectDependency(getFactory(), create(":compose-routing")); }

    /**
     * Creates a project dependency on the project at path ":innertube"
     */
    public InnertubeProjectDependency getInnertube() { return new InnertubeProjectDependency(getFactory(), create(":innertube")); }

    /**
     * Creates a project dependency on the project at path ":ktor-client-brotli"
     */
    public KtorClientBrotliProjectDependency getKtorClientBrotli() { return new KtorClientBrotliProjectDependency(getFactory(), create(":ktor-client-brotli")); }

    /**
     * Creates a project dependency on the project at path ":kugou"
     */
    public KugouProjectDependency getKugou() { return new KugouProjectDependency(getFactory(), create(":kugou")); }

}
