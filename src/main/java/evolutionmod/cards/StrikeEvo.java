package evolutionmod.cards;

import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.patches.AbstractCardEnum;

import java.util.stream.Collectors;

public class StrikeEvo
        extends AdaptableEvoCard {
    public static final String ID = "evolutionmod:StrikeEvo";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 6;
    private static final int UPGRADE_DAMAGE_AMT = 3;

    public StrikeEvo() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.BASIC, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.tags.add(BaseModCardTags.BASIC_STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        this.useAdaptations(p, m);
    }

    @Override
    public int addAdaptation(AbstractAdaptation adaptation) {
        if (!this.adaptationMap.containsKey(adaptation.getGeneId())) {
            adaptation.amount = 1;
            return super.addAdaptation(adaptation);
        }
        return 0;
    }

//    @Override
//    public void selfAdapt(AbstractPlayer player) {
//        player.orbs.stream()
//                .filter(o -> o instanceof AbstractGene)
//                .map (o -> (AbstractGene) o)
////                .filter(o -> !this.adaptationMap.containsKey(o.ID))
//                .map(AbstractGene::getAdaptation)
//                .forEach(g -> {
//                    if this::addAdaptation
//                });
//        getFilteredGenes(
//                player,
//                )
//                .stream()
//                .map(AbstractGene::getAdaptation)
//                .forEach(g -> {
//                    g.amount = 1;
//                    this.addAdaptation(g);
//                });
//    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
        }
    }
}