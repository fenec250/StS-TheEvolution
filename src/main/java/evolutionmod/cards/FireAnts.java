package evolutionmod.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.InsectGene;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.patches.AbstractCardEnum;

import java.util.List;
import java.util.stream.Collectors;

public class FireAnts
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:FireAnts";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LavafolkSkl.png";
    private static final int COST = 1;
    private static final int EVOKE_AMT = 1;
    private static final int UPGRADE_EVOKE_AMT = 1;

    public FireAnts() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
//        this.magicNumber = this.baseMagicNumber = EVOKE_AMT;
        this.cardsToPreview = new Drone();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
//        p.orbs.stream()
//                .filter(o -> LavafolkGene.ID.equals(o.ID))
//                .limit(maxEvoke)
//                .collect(Collectors.toList()) // dissociate from initial list before evoking
//                .forEach(o -> {
//                    addToBot(new EvokeSpecificOrbAction(o));
//                    addToBot(new MakeTempCardInHandAction(Drone.createDroneWithInteractions(p)));
//                });

//        for(int i = 0; i < this.magicNumber; ++i) {
//            if (isPlayerInThisForm(LavafolkGene.ID)) {
//                addToBot(new MakeTempCardInHandAction(new DroneFire()));
//            } else {
//                addToBot(new MakeTempCardInHandAction(new Drone()));
//            }
//        }
        LavafolkGene orb = new LavafolkGene();
        addToBot(new ChannelAction(orb));
        List<AbstractCard> drones = p.hand.group.stream()
                .filter(card -> Drone.ID.equals(card.cardID))
                .collect(Collectors.toList());
        drones.forEach(card -> {
            addToBot(new ExhaustSpecificCardAction(card, p.hand, true));
            orb.onEvoke();
        });
        if (!this.upgraded) {
            formEffect(InsectGene.ID, () -> addToBot(new MakeTempCardInHandAction(new Drone())));
        } else {
            addToBot(new MakeTempCardInHandAction(new Drone()));
            addToBot(new ChannelAction(new InsectGene()));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
//            this.upgradeMagicNumber(UPGRADE_EVOKE_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(InsectGene.ID)) {
//                || AbstractDungeon.player.hand.group.stream().anyMatch(c -> c instanceof AbstractDrone)) {
            this.glowColor = GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}