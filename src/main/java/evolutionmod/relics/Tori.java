package evolutionmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.BeastGene;
import evolutionmod.orbs.CentaurGene;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.orbs.InsectGene;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.orbs.LizardGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.orbs.MerfolkGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.orbs.SuccubusGene;

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
                genesPool.add(CentaurGene.ID);
                genesPool.add(PlantGene.ID);
                genesPool.add(MerfolkGene.ID);
                genesPool.add(HarpyGene.ID);
                genesPool.add(LavafolkGene.ID);
                genesPool.add(SuccubusGene.ID);
                genesPool.add(LymeanGene.ID);
                genesPool.add(InsectGene.ID);
                genesPool.add(BeastGene.ID);
                genesPool.add(LizardGene.ID);

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