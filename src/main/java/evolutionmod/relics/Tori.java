package evolutionmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.orbs.*;

import java.util.ArrayList;

public class Tori extends CustomRelic {
    public static final String ID = "evolutionmod:Tori";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final String IMG_PATH = "evolutionmod/images/relics/Tori.png";
    private static final Texture IMG = new Texture(IMG_PATH);
    public static final String OUTLINE_PATH = "evolutionmod/images/relics/Tori_p.png";
    private static final Texture OUTLINE = new Texture(OUTLINE_PATH);

    private static final int ORB_INCREASE = 1;
//
    public Tori() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStartPreDraw() {
//    public void atBattleStart() {
//        super.atBattleStart();
        this.flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new IncreaseMaxOrbAction(ORB_INCREASE));

        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                ArrayList<String> genesPool = new ArrayList<>();
                genesPool.add(CentaurGene2.ID);
                genesPool.add(PlantGene2.ID);
                genesPool.add(MerfolkGene2.ID);
                genesPool.add(HarpyGene2.ID);
                genesPool.add(LavafolkGene2.ID);
                genesPool.add(SuccubusGene2.ID);
                genesPool.add(LymeanGene2.ID);
                genesPool.add(InsectGene2.ID);
                genesPool.add(BeastGene2.ID);
                genesPool.add(LizardGene2.ID);
                genesPool.add(ShadowGene2.ID);

                long count = AbstractDungeon.player.orbs.stream()
                        .filter(o -> o instanceof EmptyOrbSlot)
                        .count();
                for (int i = 0; i < count; ++i) {

                    int index = AbstractDungeon.cardRandomRng.random(genesPool.size() - 1);
                    String geneId = genesPool.get(index);
                    genesPool.remove(index);

                    addToTop(BaseEvoCard.getGene(geneId).getChannelAction());
                }
                this.isDone = true;
            }
        });
        this.grayscale = true;
    }

    public void onVictory() {
        this.grayscale = false;
    }

    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(TorisGift.ID);
    }

    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(TorisGift.ID)) {
            for (int i=0; i<AbstractDungeon.player.relics.size(); ++i) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(TorisGift.ID)) {
                    instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
            }
        } else {
            super.obtain();
        }
    }
}