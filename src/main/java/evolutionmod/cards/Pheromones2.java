package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import evolutionmod.orbs.PlantGene2;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.powers.GrowthPower;
import evolutionmod.powers.LustPower;

public class Pheromones2
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:Pheromones";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Pheromones.png";
    private static final int COST = 1;
    private static final int DEBUFF_AMT = 2;
    private static final int UPGRADE_DEBUFF_AMT = 1;

    public Pheromones2() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.magicNumber = this.baseMagicNumber = DEBUFF_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean form = isPlayerInThisForm(PlantGene2.ID);
        AbstractDungeon.getMonsters().monsters.stream()
                .filter(mo -> !mo.isDeadOrEscaped())
                .filter(mo -> form || mo.getIntentBaseDmg() >= 0)
                .forEach(mo -> {
                    addToBot(new ApplyPowerAction(mo, p, new VulnerablePower(mo, magicNumber, false)));
                    addToBot(new ApplyPowerAction(mo, p, new LustPower(mo, magicNumber)));
                });

        formEffect(PlantGene2.ID, ()->addToBot(new ApplyPowerAction(p,p,new GrowthPower(p,1),1)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Pheromones2();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_DEBUFF_AMT);
//            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(PlantGene2.ID)) {
            this.glowColor = PlantGene2.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}